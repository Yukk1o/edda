package com.edda.auth.domain.port;

import com.edda.auth.domain.model.CodePurpose;

/**
 * 验证码发送器
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public interface VerificationCodeSender {

    /**
     * 发送验证码
     *
     * @param phone   手机号
     * @param code    验证码
     * @param purpose 用途
     */
    void send(String phone, String code, CodePurpose purpose);
}
