package com.dean.planet.wechat.entity.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文本消息
 * @author dean
 * @since 2023/3/31 10:09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TextMessageDTO extends BaseMessageDTO {

    /**
     * 文本消息内容
     */
    @XStreamAlias("Content")
    @JSONField(name = "Content")
    private String content;

}