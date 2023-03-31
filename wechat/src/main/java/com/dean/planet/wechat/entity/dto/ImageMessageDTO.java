package com.dean.planet.wechat.entity.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图片消息
 * @author dean
 * @since 2023/3/31 10:09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ImageMessageDTO extends BaseMessageDTO {
    /**
     * 图片链接（由系统生成）
     */
    @XStreamAlias("PicUrl")
    private String picUrl;

    /**
     * 图片消息媒体id，可以调用获取临时素材接口拉取数据。
     */
    @XStreamAlias("MediaId")
    private String mediaId;
}
