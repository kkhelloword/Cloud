package com.imooc.order.service.impl;

import com.imooc.order.client.ProductClient;
import com.imooc.order.dataobject.OrderDetail;
import com.imooc.order.dataobject.OrderMaster;
import com.imooc.order.dataobject.ProductInfo;
import com.imooc.order.dto.CartDto;
import com.imooc.order.enums.OrderStatusEnum;
import com.imooc.order.enums.PayStatusEnum;
import com.imooc.order.enums.RestEnum;
import com.imooc.order.exception.SellException;
import com.imooc.order.repository.OrderDetailsRepository;
import com.imooc.order.repository.OrderMasterRepository;
import com.imooc.order.service.OrderService;
import com.imooc.order.utils.KeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderMasterRepository masterRepository;

    @Autowired
    private OrderDetailsRepository detailsRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    @Transactional
    public OrderMaster create(OrderMaster orderMaster) {
        String orderId = KeyUtils.getUniqueKey();
        // 查询商品信息 (调用商品服务)
        List<String> ids = orderMaster.getOrderDetails().stream().map(OrderDetail::getProductId).collect(Collectors.toList());
        List<ProductInfo> productList = productClient.getProductList(ids);
        BigDecimal bigDecimal = new BigDecimal(BigInteger.ZERO);
        //计算总价
        for (OrderDetail orderDetail : orderMaster.getOrderDetails()) {
            for (ProductInfo productInfo : productList) {
                if (orderDetail.getProductId().equals(productInfo.getProductId())){
                    bigDecimal = productInfo.getProductPrice()
                            .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(bigDecimal);
                    // 订单详情入库
                    BeanUtils.copyProperties(productInfo,orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtils.getUniqueKey());
                    detailsRepository.save(orderDetail);
                }
            }
        }
        //减库存（调用商品服务）
        List<CartDto> cartDtoList = orderMaster.getOrderDetails().stream().map(e -> new CartDto(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());
        productClient.decreaseStock(cartDtoList);
        // 订单入库
        OrderMaster master = new OrderMaster();
        orderMaster.setOrderId(orderId);
        BeanUtils.copyProperties(orderMaster,master);
        master.setOrderAmount(bigDecimal);
        master.setOrderStatus(OrderStatusEnum.NEW.getCode());
        master.setPayStatus(PayStatusEnum.WAIT.getCode());

        masterRepository.save(master);
        return orderMaster;
    }

    @Override
    @Transactional
    public OrderMaster findOne(String orderId) {
        OrderMaster orderMaster = masterRepository.findById(orderId).orElse(null);
        if (orderMaster == null){
            throw new SellException(RestEnum.ORDER_NOT_EXIST);
        }
        // 根据订单编号查询订单详情
        List<OrderDetail> detailList = detailsRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(detailList)){
            throw new SellException(RestEnum.ORDER_NOT_EXIST);
        }
        orderMaster.setOrderDetails(detailList);
        return orderMaster;
    }


    @Override
    @Transactional
    public OrderMaster finish(String orderId) {
        // 查询订单
        OrderMaster orderMaster = masterRepository.findById(orderId).orElse(null);
        // 判断订单状态
        if (!orderMaster.getOrderStatus().getCode().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【完结订单】订单状态不正确 ordermaster={}",orderMaster);
            throw new SellException(RestEnum.ORDER_STATUS_ERROR);
        }
        // 修改订单状态
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster save = masterRepository.save(orderMaster);
        if (save == null){
            log.error("【完结订单】订单更新失败.orderMaster={}",orderMaster);
            throw new SellException(RestEnum.ORDER_UPDATE_FAIL);
        }
        // 查询订单详情
        List<OrderDetail> orderDetails = detailsRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetails)){
            throw new SellException(RestEnum.ORDER_DETAIL_EMPTY);
        }
        OrderMaster orderMasterNew = new OrderMaster();
        BeanUtils.copyProperties(orderMaster,orderMasterNew);
        orderMasterNew.setOrderDetails(orderDetails);
        return orderMasterNew;
    }
}
