package com.imooc.order.exception;

import com.imooc.order.enums.RestEnum;
import lombok.Getter;

/**
 * @Author: baiyj
 * @Date: 2019/10/15
 */

@Getter
public class SellException extends RuntimeException{

    private Integer code;

    public SellException(RestEnum restEnum){
        super(restEnum.getMessage());
        this.code = restEnum.getCode();
    }
    public SellException(Integer code,String Message){
        super(Message);
        this.code = code;
    }
}
