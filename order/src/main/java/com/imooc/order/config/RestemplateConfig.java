package com.imooc.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate getRestemplate(){
        return new RestTemplate();
    }

}
