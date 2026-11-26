package com.edda.common;

import lombok.Getter;

/**
 * 业务异常
 *
 * <p>预期内的失败,不打堆栈,warn 级别即可
 * 非预期异常交给兜底处理,打 error 加全堆栈
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
@Getter
public class BizException extends RuntimeException {

    private final int code;

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BizException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}
