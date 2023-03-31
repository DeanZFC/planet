package com.dean.planet.wechat.entity.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dean
 * @since 2023/3/23 17:57
 */
@Data
@XStreamAlias("xml")
public class BaseMessageDTO implements Serializable {
    /**
     * 开发者微信号
     */
    @XStreamAlias("ToUserName")
    @JSONField(name = "ToUserName")
    private String toUserName;

    /**
     * 发送方帐号（一个OpenID）
     */
    @XStreamAlias("FromUserName")
    @JSONField(name = "FromUserName")
    private String fromUserName;

    /**
     * 消息创建时间 （整型）
     */
    @XStreamAlias("CreateTime")
    @JSONField(name = "CreateTime")
    private Long createTime;

    /**
     * 消息类型，(文本:text,图片-image,语音:voice,视频:video,小视频:shortvideo,地理位置:location,链接:link,事件:event)
     */
    @XStreamAlias("MsgType")
    @JSONField(name = "MsgType")
    private String msgType;


    /**
     * 消息id，64位整型
     */
    @JSONField(name = "MsgId")
    @XStreamAlias("MsgId")
    private String msgId;

    /**
     * 消息的数据ID（消息如果来自文章时才有）
     */
    @XStreamAlias("MsgDataId")
    @JSONField(name = "MsgDataId")
    private String msgDataId;

    /**
     * 多图文时第几篇文章，从1开始（消息如果来自文章时才有）
     */
    @XStreamAlias("Idx")
    @JSONField(name = "Idx")
    private String idx;

}
