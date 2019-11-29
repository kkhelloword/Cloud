package com.imooc.product.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购物车
 *
 * @Author: baiyj
 * @Date: 2019/10/15
 */

@Data
@NoArgsConstructor
public class CartDto {
    /*商品id*/
    private String productId;
    /*商品数量*/
    private Integer productQuantity;

    public CartDto(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
