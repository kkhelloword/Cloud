package com.imooc.service.service;

import com.imooc.service.dataobject.UserInfo;
import com.imooc.service.repository.UserInfoRepostory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserInfoRepostory userInfoRepostory;

    public UserInfo findUserByOpenid(String openid){
        return userInfoRepostory.findByOpenid(openid);
    }
}
