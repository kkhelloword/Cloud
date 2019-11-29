package com.imooc.order.service;

import com.imooc.order.dataobject.OrderMaster;

/**
 * 买家
 * @Author: baiyj
 * @Date: 2019/10/16
 */
public interface BuyerService {

    // 查询一个订单
    OrderMaster findOrderOne(String openid, String orderId);
    //取消订单
    OrderMaster cancelOrder(String openid, String orderId);

}
