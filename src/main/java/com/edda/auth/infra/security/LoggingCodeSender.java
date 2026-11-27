package com.edda.auth.infra.security;

import com.edda.auth.domain.model.CodePurpose;
import com.edda.auth.domain.port.VerificationCodeSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingCodeSender implements VerificationCodeSender {
    @Override
    public void send(String phone, String code, CodePurpose purpose) {
        log.info("发送验证码: phone={}, purpose={}, code={}", phone, purpose,
                code);
    }
}
