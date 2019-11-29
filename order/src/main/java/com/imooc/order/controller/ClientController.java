package com.imooc.order.controller;

import com.imooc.order.client.ProductClient;
import com.imooc.order.dataobject.ProductInfo;
import com.imooc.order.dto.CartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


/**
 * rest远程调用测试
 * @Author: baiyj
 * @Date: 2019/10/30
 */
@RestController
public class ClientController {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductClient productClient;

    @GetMapping("/server")
     String getmsg(){
//        RestTemplate restTemplate = new RestTemplate();
        /*第一种直接restemplate*/
//        String s = restTemplate.getForObject("http://127.0.0.1:8081/server", String.class);
//        System.out.println(s);

        /*第二种 使用cloud的LoadBalancerClient负载均衡器*/
//        ServiceInstance instance = loadBalancerClient.choose("PRODUCT");
//        String url = String.format("http://%s:%s",instance.getHost(),instance.getPort()+"/server");
//        String s = restTemplate.getForObject(url, String.class);
//        System.out.println(s);

        /*第三种 使用@LoadBalanced注解*/
//        String s = restTemplate.getForObject("http://PRODUCT/server", String.class);

        String msg = productClient.getmsg();
        System.out.println(msg);
        return msg;
    }

    @PostMapping("/productList")
    List<ProductInfo> getProductList(List<String> productIds){
       return productClient.getProductList(productIds);
    }

    @PostMapping("/ProductDecreaseStock")
    void decreaseStock(){
        productClient.decreaseStock(Arrays.asList(new CartDto("123",5)));
    }
}
