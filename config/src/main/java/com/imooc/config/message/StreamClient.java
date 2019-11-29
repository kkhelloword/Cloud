package com.imooc.config.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StreamClient {

    @Output("OnputMessage")
    MessageChannel output();

    @Input("OnputMessage2")
    SubscribableChannel input2();
}
