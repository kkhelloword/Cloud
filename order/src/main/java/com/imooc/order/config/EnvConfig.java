package com.imooc.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Data
@ConfigurationProperties("girl")
@Component
@RefreshScope
public class EnvConfig {
    private String name;
    private String age;
}
