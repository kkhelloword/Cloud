package com.imooc.product.controller;

import com.imooc.product.dataobject.ProductInfo;
import com.imooc.product.enums.RestEnum;
import com.imooc.product.exception.SellException;
import com.imooc.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ServerController {

    @Autowired
    private ProductService productService;

    @GetMapping("/server")
    public String server(){
        return "this is server";
    }


}

