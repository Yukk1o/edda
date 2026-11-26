package com.edda.auth.api;

import jakarta.validation.constraints.NotBlank;

/**
 * 短信登录请求
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public record SmsLoginReq(
        @NotBlank(message = "手机号不能为空") String phone,
        @NotBlank(message = "验证码不能为空") String code
) {
}
