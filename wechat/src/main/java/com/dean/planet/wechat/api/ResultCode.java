package com.dean.planet.wechat.api;

import lombok.AllArgsConstructor;

/**
 * 枚举了一些常用API操作码
 * @author dean
 * @since 2023/3/31 16:17
 */
@AllArgsConstructor
public enum ResultCode implements IResultCode {
    /**
     * 操作码
     */
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限");
    private final Integer code;
    private final String message;


    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
