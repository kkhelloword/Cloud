package com.imooc.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.imooc.product.dataobject.ProductInfo;
import com.imooc.product.dto.CartDto;
import com.imooc.product.enums.ProductStatusEnum;
import com.imooc.product.enums.RestEnum;
import com.imooc.product.exception.SellException;
import com.imooc.product.repository.ProductInfoRepository;
import com.imooc.product.service.ProductService;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductInfoRepository repository;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public ProductInfo findOne(String productId) {
        return repository.findById(productId).orElse(null);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(PageRequest pageRequest) {
        return repository.findAll(pageRequest);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDto> cartDtoList) {
        for (CartDto cartDto : cartDtoList) {
            ProductInfo productInfo = repository.findById(cartDto.getProductId()).orElse(null);
            if (productInfo == null){
                throw new SellException(RestEnum.PRODUCT_NO_EXIST);
            }
            int result = productInfo.getProductStock() + cartDto.getProductQuantity();
            if (result < 0){
                throw new SellException(RestEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(result);
            repository.save(productInfo);
        }
    }

    @Override
    public void decreaseStock(List<CartDto> cartDtoList) {
        List<ProductInfo> list = decreaseStockMethod(cartDtoList);
        // 发送mq消息 amqpTemplate.convertAndSend("队列名"，“消息内容”)
            amqpTemplate.convertAndSend("productInfo", JSON.toJSONString(list));

    }

    @Transactional
    public List<ProductInfo> decreaseStockMethod(List<CartDto> cartDtoList) {
        ArrayList<ProductInfo> productInfos = new ArrayList<>();
        for (CartDto cartDto : cartDtoList) {
            ProductInfo productInfo = repository.findById(cartDto.getProductId()).orElse(null);
            if (productInfo == null){
                throw new SellException(RestEnum.PRODUCT_NO_EXIST);
            }
            int result = productInfo.getProductStock() - cartDto.getProductQuantity();
            if (result < 0){
                throw new SellException(RestEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(result);
            repository.save(productInfo);
            productInfos.add(productInfo);
        }
        return productInfos;
    }

    @Override
    public ProductInfo onSale(String productId) {
    /*    ProductInfo productInfo = repository.findById(productId).orElse(null);
        if (productInfo == null){
            throw new SellException(RestEnum.PRODUCT_NO_EXIST);
        }
        if (productInfo.getProductStatus() == ProductStatusEnum.UP){
            throw new SellException(RestEnum.PRODUCT_STATUS_ERROR);
        }
        *//*修改状态*//*
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        return repository.save(productInfo);*/
        return null;
    }

    @Override
    public ProductInfo offSale(String productId) {
     /*   ProductInfo productInfo = repository.findById(productId).orElse(null);
        if (productInfo == null){
            throw new SellException(RestEnum.PRODUCT_NO_EXIST);
        }
        if (productInfo.getProductStatus().getCode() == ProductStatusEnum.DOWN.getCode()){
            throw new SellException(RestEnum.PRODUCT_STATUS_ERROR);
        }
        *//*修改状态*//*
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return repository.save(productInfo);*/
        return null;
    }

    @Override
    public List<ProductInfo> getProductList(List<String> productIds) {
      return repository.findByProductIdIn(productIds);
    }
}
