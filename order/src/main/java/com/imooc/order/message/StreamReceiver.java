package com.imooc.order.message;

import com.imooc.order.dataobject.OrderMaster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableBinding(StreamClient.class)
public class StreamReceiver {

    @StreamListener("OnputMessage")
    @SendTo("OnputMessage2")
    public String process(OrderMaster message){
        log.info("StreamReceiver:{}",message.getName()+message.getAge());
        return "returnInfo";
    }

//    @StreamListener("OnputMessage2")
//    public void process2(String message){
//        System.out.println("OnputMessage2===="+message);
//    }
}
