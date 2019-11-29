package com.imooc.order.service.impl;

import com.imooc.order.dataobject.OrderMaster;
import com.imooc.order.enums.RestEnum;
import com.imooc.order.exception.SellException;
import com.imooc.order.service.BuyerService;
import com.imooc.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {
    @Autowired
    private OrderService orderService;

    @Override
    public OrderMaster findOrderOne(String openid, String orderId) {
        return checkOrderOwner(openid, orderId);
    }

    @Override
    public OrderMaster cancelOrder(String openid, String orderId) {
        OrderMaster master = checkOrderOwner(openid, orderId);
        if (master == null) {
            log.error("【取消订单】 查不到该订单，orderId={}", orderId);
            throw new SellException(RestEnum.ORDER_NOT_EXIST);
        }
        return master;
    }

    private OrderMaster checkOrderOwner(String openid, String orderId) {
        OrderMaster orderMaster = orderService.findOne(orderId);
        // 判断是否是自己的订单
        if (!orderMaster.getBuyerOpenid().equalsIgnoreCase(openid)) {
            log.error("【查询订单】订单的openid不一致,openid={},orderMaster={}", openid, orderMaster);
            throw new SellException(RestEnum.ORDER_OWNER_ERROR);
        }
        return orderMaster;
    }


}
