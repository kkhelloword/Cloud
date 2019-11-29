package com.imooc.config;

import com.imooc.config.message.OrderDto;
import com.imooc.config.message.StreamClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@EnableBinding(StreamClient.class)
public class SendMessageController {

    @Autowired
    private StreamClient streamClient;

    @GetMapping("/sendMessage")
    public void process(){
        OrderDto orderDto = new OrderDto();
        orderDto.setName("zhangsan");
        orderDto.setAge("18");
        streamClient.output().send(MessageBuilder.withPayload(orderDto).build());
    }

    @StreamListener("OnputMessage2")
    public void process2(String message){
        System.out.println("OnputMessage2===="+message);
    }

    @GetMapping("test")
    public void test(){
        System.out.println("test 成功");
    }
}