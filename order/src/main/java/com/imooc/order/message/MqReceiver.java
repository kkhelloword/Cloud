package com.imooc.order.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.imooc.order.dataobject.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * rebbitmq 使用
 * @Author: baiyj
 * @Date: 2019/11/6
 */
@Component
@Slf4j
public class MqReceiver {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static  final   String PRODUCT_STOCK_FORMAT="PRODUCT_STOCK_%s";

    @RabbitListener(queuesToDeclare = @Queue("MyQueue"))
    public void process(String message){
        System.out.println("MQ接收的消息是:"+message);
    }

    @RabbitListener(bindings =@QueueBinding(
            exchange=@Exchange("MyOrder"),
            value = @Queue("computerOrder"),
            key = "computer"
    ))
    public void exchange(String message){
        System.out.println("电脑接收消息是:"+message);
    }

    @RabbitListener(bindings =@QueueBinding(
            exchange=@Exchange("MyOrder"),
            value = @Queue("fruitOrder"),
            key = "fruit"
    ))
    public void fruit(String message){
        System.out.println("水果接收消息是:"+message);
    }

    @RabbitListener(queuesToDeclare = @Queue("productInfo"))
    public void decreaseStock(String message){
        List<ProductInfo> productInfo = JSON.parseObject(message, new TypeReference<List<ProductInfo>>(){});
        log.info("【收到productInfo队列的消息】,productInfo={}",productInfo);

        for (ProductInfo info : productInfo) {
            //将库存存储到redis数据库
            stringRedisTemplate.opsForValue().set(String.format(PRODUCT_STOCK_FORMAT,info.getProductId()),
                    String.valueOf(info.getProductStock()));
        }

    }
}
