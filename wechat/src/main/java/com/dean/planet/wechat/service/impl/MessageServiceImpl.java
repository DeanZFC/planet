package com.dean.planet.wechat.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dean.planet.wechat.entity.dto.TextMessageDTO;
import com.dean.planet.wechat.entity.http.param.OpenAiQuestionParam;
import com.dean.planet.wechat.service.MessageService;
import com.dean.planet.wechat.utils.HttpClientUtils;
import com.dean.planet.wechat.utils.WechatMessageUtils;
import com.dean.planet.wechat.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author dean
 * @since 2023/3/31 11:44
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    @Override
    public String dealTextMessage(TextMessageDTO textMessage) throws IOException {
        log.info("收到文本消息:{}", JSON.toJSONString(textMessage));
        //收到的文本消息
        String content = textMessage.getContent();
        //发送的文本消息
        String sendContent = this.sendOpenAi(content);


        //获取回复默认文本消息实体类
        TextMessageDTO sendTestMessage = WechatMessageUtils.getDefaultTextMessage(textMessage.getFromUserName(), textMessage.getToUserName());
        sendTestMessage.setContent("你好");
        return XmlUtil.entityToXml(sendTestMessage);
    }

    private String sendOpenAi(String content) throws IOException {

        OpenAiQuestionParam param = new OpenAiQuestionParam();
        param.setPrompt(content);
        JSONObject jsonObject = HttpClientUtils.jsonPost("https://api.openai.com/v1/completions", JSON.toJSONString(param), JSONObject.class);
        return jsonObject.getString("choices");
    }
}
