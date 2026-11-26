package com.edda.auth.app;

import com.edda.auth.domain.model.CodePurpose;
import com.edda.auth.domain.model.CredentialRequest;
import org.springframework.stereotype.Service;

/**
 * 认证服务占位实现
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public LoginResult loginByCredential(CredentialRequest request) {
        throw new UnsupportedOperationException("TODO 按 issue #8 实现短信登录与密码登录");
    }

    @Override
    public void sendCode(String phone, CodePurpose purpose) {
        throw new UnsupportedOperationException("TODO 按 issue #7 实现发码");
    }
}
