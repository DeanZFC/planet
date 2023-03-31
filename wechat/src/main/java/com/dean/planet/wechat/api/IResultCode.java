package com.dean.planet.wechat.api;

/**
 * 封装API的状态码
 * @author dean
 * @since 2023/3/31 16:19
 */
public interface IResultCode {
    /**
     * 获取错误码
     * @return 错误码
     */
    Integer getCode();

    /**
     * 获取错误信息
     * @return 错误信息
     */
    String getMessage();
}
