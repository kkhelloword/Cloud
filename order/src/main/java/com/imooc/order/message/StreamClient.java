package com.imooc.order.message;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface StreamClient {

    @Input("OnputMessage")
    SubscribableChannel input();

//    @Input("OnputMessage2")
//    SubscribableChannel input2();

//    @Output("OnputMessage")
//    MessageChannel output();
}
