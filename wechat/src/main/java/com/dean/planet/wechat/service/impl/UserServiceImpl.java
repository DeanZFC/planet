package com.dean.planet.wechat.service.impl;

import com.alibaba.fastjson2.JSON;
import com.dean.planet.wechat.constants.enums.WechatMsgTypeEnum;
import com.dean.planet.wechat.entity.dto.TextMessageDTO;
import com.dean.planet.wechat.entity.dto.WechatAuthorizeDTO;
import com.dean.planet.wechat.service.MessageService;
import com.dean.planet.wechat.service.UserService;
import com.dean.planet.wechat.utils.XmlUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author dean
 * @since 2023/3/31 10:20
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final MessageService messageService;

    @Override
    public String checkSignature(WechatAuthorizeDTO wechatAuthorizeDto) {
        return wechatAuthorizeDto.getEchostr();
    }

    @Override
    public String autoReply(HttpServletRequest request) throws IOException {
        Map<String, String> params = XmlUtil.XmlToMap(request.getInputStream());
        log.info("callback baseMessage:{}", JSON.toJSONString(params));
        String msgType = MapUtils.getString(params, "MsgType");
        if (WechatMsgTypeEnum.TEXT.getValue().equals(msgType)) {
            TextMessageDTO textMessage = JSON.parseObject(JSON.toJSONString(params), TextMessageDTO.class);
            return messageService.dealTextMessage(textMessage);
        }
        return null;
    }
}
