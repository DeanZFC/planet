package com.dean.planet.wechat.entity.http.param;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 
 * @author dean
 * @since 2023/3/31 16:46
 */
@Data
public class OpenAiQuestionParam {
    private String model = "text-davinci-003";

    private String prompt;

    @JSONField(name = "max_tokens")
    private Integer maxTokens = 256;

    private float temperature = 0.5f;

    @JSONField(name = "top_p")
    private Integer topP = 1;

    private Integer n = 1;

    private Boolean stream = false;

    @JSONField(name = "logprobs")
    private Boolean logProbs;

    private String stop;
}