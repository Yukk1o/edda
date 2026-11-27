package com.edda.auth.app;

import com.edda.auth.domain.model.CodePurpose;
import com.edda.auth.domain.model.CredentialRequest;
import com.edda.auth.domain.port.VerificationCodeSender;
import com.edda.auth.domain.port.VerificationCodeStore;
import com.edda.common.BizException;
import com.edda.common.ErrorCode;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证服务占位实现
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Duration CODE_TTL =
            Duration.ofMinutes(5);
    private static final Duration
            SEND_INTERVAL = Duration.ofSeconds(60);
    private static final SecureRandom
            SECURE_RANDOM = new SecureRandom();

    private final VerificationCodeStore
            codeStore;
    private final VerificationCodeSender
            codeSender;
    private final ConcurrentHashMap<String,
            Instant> lastSentAt = new
            ConcurrentHashMap<>();

    public
    AuthServiceImpl(VerificationCodeStore
                            codeStore, VerificationCodeSender
                            codeSender) {
        this.codeStore = codeStore;
        this.codeSender = codeSender;
    }

    @Override
    public LoginResult loginByCredential(CredentialRequest request) {
        throw new UnsupportedOperationException("TODO 按 issue #8 实现短信登录与密码登录");
    }

    @Override
    public void sendCode(String phone, CodePurpose purpose) {
        String rateLimitKey = phone + ":" +
                purpose.code();
        Instant now = Instant.now();

        lastSentAt.compute(rateLimitKey, (k,
                                          last) -> {
            if (last != null &&
                    Duration.between(last,
                            now).compareTo(SEND_INTERVAL) <
                            0) {
                throw new
                        BizException(ErrorCode.BAD_REQUEST,
                        "发送过于频繁,请稍后再试");
            }
            return now;
        });

        String code = String.format("%06d",
                SECURE_RANDOM.nextInt(1_000_000));
        codeStore.save(phone, purpose, code,
                CODE_TTL);
        codeSender.send(phone, code,
                purpose);
    }
}
