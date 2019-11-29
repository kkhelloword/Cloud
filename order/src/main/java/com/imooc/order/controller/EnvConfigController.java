package com.imooc.order.controller;


import com.imooc.order.config.EnvConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("name")
public class EnvConfigController {

    @Autowired
    private EnvConfig envConfig;

    @GetMapping("name")
    public String getAttr(){
        return "name:"+envConfig.getName()+"   age:"+envConfig.getAge();
    }
}
