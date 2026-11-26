package com.edda.auth.api;

import jakarta.validation.constraints.NotBlank;

/**
 * 发送验证码请求
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public record SendCodeReq(
        @NotBlank(message = "手机号不能为空") String phone
) {
}
