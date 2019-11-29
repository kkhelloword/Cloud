package com.imooc.service.controller;

import com.imooc.service.VO.ResultVO;
import com.imooc.service.constant.CookieConstant;
import com.imooc.service.constant.RedisConstant;
import com.imooc.service.dataobject.UserInfo;
import com.imooc.service.enumClass.ResultEnum;
import com.imooc.service.enumClass.RoleEnum;
import com.imooc.service.service.UserService;
import com.imooc.service.utils.CookieUtils;
import com.imooc.service.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/buyer")
    public ResultVO queryuser(@RequestParam("openid")String openid, HttpServletResponse response){

        //1. 查询数据库 openid
        UserInfo userByOpenid = userService.findUserByOpenid(openid);
        if (userByOpenid == null){
            return ResultVOUtil.error(ResultEnum.USER_FAIL);
        }
        // 2. 查询角色类型是否正确
        if (userByOpenid.getRole() != RoleEnum.BUYER.getCode()){
            return ResultVOUtil.error(ResultEnum.ROLE_FAIL);
        }
        //3. cookie里设置openid
        CookieUtils.set(response, CookieConstant.OPENID,openid,CookieConstant.EXPIRE);
        return ResultVOUtil.success();
    }

    @GetMapping("/seller")
    public ResultVO querySellerUser(@RequestParam("openid")String openid,
                                    HttpServletResponse response,
                                    HttpServletRequest request){
        // 判断用户是否登录，如果登录过就不需要继续执行
        Cookie cookie = CookieUtils.get(request, CookieConstant.TOKEN);
        if (cookie!=null &&
                !StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN,cookie.getValue())))){
            return ResultVOUtil.success();
        }
        //1. 查询数据库 openid
        UserInfo userByOpenid = userService.findUserByOpenid(openid);
        if (userByOpenid == null){
            return ResultVOUtil.error(ResultEnum.USER_FAIL);
        }
        // 2. 查询角色类型是否正确
        if (userByOpenid.getRole() != RoleEnum.SELLER.getCode()){
            return ResultVOUtil.error(ResultEnum.ROLE_FAIL);
        }
        // 3. redis设置uuid，value是openid
        String token = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN,token),
                openid,
                CookieConstant.EXPIRE,
                TimeUnit.SECONDS);
        //4. cookie里设置openid
        CookieUtils.set(response, CookieConstant.TOKEN,token,CookieConstant.EXPIRE);
        return ResultVOUtil.success();
    }
}
