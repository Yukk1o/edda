package com.edda.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回结果
 *
 * <p>约定:业务失败也返回 HTTP 200,靠 {@code code} 区分
 * 前端只需要判断一处,不用同时处理 HTTP 状态和业务码两套逻辑
 *
 * <p>方法名用 {@code ok()}
 *
 * @param <T> 数据类型
 * @author Yukk1o
 * @since 2026-11-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public boolean ok() {
        return code != null && code == 200;
    }
}
