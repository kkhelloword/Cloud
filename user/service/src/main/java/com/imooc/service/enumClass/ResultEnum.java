package com.imooc.service.enumClass;

import lombok.Getter;

@Getter
public enum ResultEnum  {
    USER_FAIL(0,"用户查询失败"),
    ROLE_FAIL(1,"角色匹配失败");

    private Integer code;
    private String msg;

    ResultEnum(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
