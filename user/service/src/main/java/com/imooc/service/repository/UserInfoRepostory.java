package com.imooc.service.repository;


import com.imooc.service.dataobject.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepostory extends JpaRepository<UserInfo,String> {

    UserInfo findByOpenid(String openid);

}
