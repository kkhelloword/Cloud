package com.imooc.order.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.imooc.order.dataobject.OrderDetail;
import com.imooc.order.dataobject.OrderMaster;
import com.imooc.order.form.OrderForm;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class OrderForm2OrderMaster {

    public static OrderMaster ConvertOrderMaster(OrderForm orderForm){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setBuyerName(orderForm.getName());
        orderMaster.setBuyerAddress(orderForm.getAddress());
        orderMaster.setBuyerPhone(orderForm.getPhone());
        orderMaster.setBuyerOpenid(orderForm.getOpenid());
        List<OrderDetail> orderDetailList = null;
        try {
            orderDetailList = JSON.parseObject(orderForm.getItems(), new TypeReference<List<OrderDetail>>(){});
        } catch (Exception e) {
            log.error("【对象转换】错误，,String={}",orderForm.getItems());
            e.printStackTrace();
        }
        orderMaster.setOrderDetails(orderDetailList);
        return orderMaster;
    }
}
