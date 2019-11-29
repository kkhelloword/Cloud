package com.imooc.order.controller;

import com.imooc.order.VO.ResultVO;
import com.imooc.order.convert.OrderForm2OrderMaster;
import com.imooc.order.dataobject.OrderMaster;
import com.imooc.order.enums.RestEnum;
import com.imooc.order.exception.SellException;
import com.imooc.order.form.OrderForm;
import com.imooc.order.service.BuyerService;
import com.imooc.order.service.OrderService;
import com.imooc.order.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    // 创建订单
    @RequestMapping("create")
    public ResultVO<Map<String,String>> create  (@Valid OrderForm form,
                                               BindingResult bindResult){
        if (bindResult.hasErrors()){
            log.error("【创建订单】 请求参数不正确,orderForm={}",form);
            throw new SellException(RestEnum.PARAM_ERROR.getCode(),bindResult.getFieldError().getDefaultMessage());
        }
        OrderMaster orderMaster = OrderForm2OrderMaster.ConvertOrderMaster(form);
        if (CollectionUtils.isEmpty(orderMaster.getOrderDetails())){
            log.error("【创建订单】 购物车不能为空");
            throw new SellException(RestEnum.CART_EMPTY);
        }
        OrderMaster master = orderService.create(orderMaster);
        // 拼装数据
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderId",master.getOrderId());
        return ResultVOUtil.success(hashMap);
    }

    /** 完结订单
     * @param [orderId, model]
     * @return java.lang.String
     */
    @PostMapping("/finish")
    public ResultVO<OrderMaster> finish(@RequestParam(value = "orderId") String orderId) {
       return ResultVOUtil.success(orderService.finish(orderId));
    }
}
