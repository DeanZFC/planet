package com.dean.planet.wechat.entity.dto;

import lombok.Data;

/**
 * @author dean
 * @since 2023/3/23 17:26
 */
@Data
public class WechatAuthorizeDTO {

    private String signature;

    private String timestamp;

    private String nonce;

    private String echostr;
}
