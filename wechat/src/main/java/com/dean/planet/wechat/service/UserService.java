package com.dean.planet.wechat.service;

import com.dean.planet.wechat.entity.dto.WechatAuthorizeDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * @author dean
 * @since 2023/3/31 10:20
 */
public interface UserService {
    /**
     * 验证签名
     * @param wechatAuthorizeDto 微信授权信息
     * @return 验证结果 返回echoStr则验证成功
     */
    String checkSignature(WechatAuthorizeDTO wechatAuthorizeDto);

    /**
     * 自动回复
     * @param request request
     * @return 回复内容
     */
    String autoReply(HttpServletRequest request) throws IOException;
}
