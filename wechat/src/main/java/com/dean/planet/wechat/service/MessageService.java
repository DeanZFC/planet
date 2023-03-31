package com.dean.planet.wechat.service;

import com.dean.planet.wechat.entity.dto.TextMessageDTO;

import java.io.IOException;

/**
 * @author dean
 * @since 2023/3/31 11:44
 */
public interface MessageService {
    /**
     * 处理文本消息
     * @param textMessage 文本消息
     * @return 回复内容
     */
    String dealTextMessage(TextMessageDTO textMessage) throws IOException;
}
