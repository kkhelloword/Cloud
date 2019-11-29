package com.imooc.order.client;


import com.imooc.order.dataobject.ProductInfo;
import com.imooc.order.dto.CartDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "PRODUCT")
public interface ProductClient {

    @GetMapping("/server")
     String getmsg();

    @PostMapping("/product/productList")
    List<ProductInfo> getProductList(List<String> productIds);

    @PostMapping("/product/decreaseStock")
    void decreaseStock(@RequestBody List<CartDto> cartDtos);
}
