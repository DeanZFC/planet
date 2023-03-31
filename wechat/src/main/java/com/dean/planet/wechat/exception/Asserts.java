package com.dean.planet.wechat.exception;

import com.dean.planet.wechat.api.IResultCode;

/**
 * 断言处理类，用于抛出各种API异常
 * @author dean
 * @since 2023/3/31 16:33
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IResultCode errorCode) {
        throw new ApiException(errorCode);
    }
}
