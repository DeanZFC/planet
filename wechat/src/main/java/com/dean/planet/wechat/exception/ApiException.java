package com.dean.planet.wechat.exception;

import com.dean.planet.wechat.api.IResultCode;

/**
 * 自定义API异常
 * @author dean
 * @since 2023/3/31 16:13
 */
public class ApiException extends RuntimeException {
    private IResultCode errorCode;

    public ApiException(IResultCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public IResultCode getErrorCode() {
        return errorCode;
    }
}
