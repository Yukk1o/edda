package com.edda.auth.app;

import com.edda.auth.domain.model.CodePurpose;
import com.edda.auth.domain.model.CredentialRequest;

/**
 * 认证服务
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public interface AuthService {

    /**
     * 通过凭据登录
     *
     * @param request 凭据请求
     * @return 登录结果
     */
    LoginResult loginByCredential(CredentialRequest request);

    /**
     * 发送验证码
     *
     * @param phone   手机号
     * @param purpose 用途,不同用途的码不能互用
     */
    void sendCode(String phone, CodePurpose purpose);
}
