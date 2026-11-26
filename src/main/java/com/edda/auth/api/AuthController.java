package com.edda.auth.api;

import com.edda.auth.app.AuthService;
import com.edda.auth.app.LoginResult;
import com.edda.auth.domain.model.CodePurpose;
import com.edda.auth.domain.model.CredentialRequest;
import com.edda.auth.domain.model.LoginProvider;
import com.edda.common.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 *
 * <p>只做协议转换,不放业务判断
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 发送登录验证码
     */
    @PostMapping("/code")
    public Result<Void> sendLoginCode(@RequestBody @Valid SendCodeReq req) {
        authService.sendCode(req.phone(), CodePurpose.LOGIN);
        return Result.success();
    }

    /**
     * 短信登录
     */
    @PostMapping("/login/sms")
    public Result<LoginResult> loginBySms(@RequestBody @Valid SmsLoginReq req) {
        CredentialRequest request = new CredentialRequest(LoginProvider.SMS, req.phone(), req.code());
        return Result.success(authService.loginByCredential(request));
    }
}
