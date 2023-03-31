package com.dean.planet.wechat.controller;

import com.alibaba.fastjson2.JSON;
import com.dean.planet.wechat.entity.dto.WechatAuthorizeDTO;
import com.dean.planet.wechat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author dean
 * @since 2023/3/23 17:21
 */
@RestController
@RequestMapping("/user")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 微信接入连接校验
     * @param wechatAuthorizeDto 授权信息
     * @return echoStr
     */
    @GetMapping("/callback")
    public String callback(WechatAuthorizeDTO wechatAuthorizeDto){
        log.info("callback wechatAuthorizeDto:{}", JSON.toJSONString(wechatAuthorizeDto));
        return userService.checkSignature(wechatAuthorizeDto);

    }

    /**
     * 微信公众号自动回复
     * @param request request
     */
    @PostMapping("/callback")
    public String callback(HttpServletRequest request) throws IOException {
        return userService.autoReply(request);
    }
}
