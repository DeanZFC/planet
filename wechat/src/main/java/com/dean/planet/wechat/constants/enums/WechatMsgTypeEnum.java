package com.dean.planet.wechat.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信消息类型
 * @author dean
 * @since 2023/3/31 10:29
 */
@AllArgsConstructor
@Getter
public enum WechatMsgTypeEnum {

    /**
     * 微信消息类型
     */
    TEXT("text", "文本消息");

    private final String value;

    private final String name;

}
