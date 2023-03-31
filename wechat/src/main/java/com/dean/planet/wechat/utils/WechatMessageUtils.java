package com.dean.planet.wechat.utils;

import com.dean.planet.wechat.constants.enums.WechatMsgTypeEnum;
import com.dean.planet.wechat.entity.dto.TextMessageDTO;

/**
 *
 * @author dean
 * @since 2023/3/31 10:03
 */
public class WechatMessageUtils {

    private WechatMessageUtils() {
    }

    /**
     * 获取默认文本消息
     *
     * @param receiver 接收人
     * @param officialWxId 官方微信id
     * @return 文本消息
     */
    public static TextMessageDTO getDefaultTextMessage(String receiver, String officialWxId) {
        TextMessageDTO textMessage = new TextMessageDTO();
        textMessage.setToUserName(receiver);
        textMessage.setFromUserName(officialWxId);
        textMessage.setCreateTime(System.currentTimeMillis());
        textMessage.setMsgType(WechatMsgTypeEnum.TEXT.getValue());
        return textMessage;
    }

}