package com.edda.auth.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户身份
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
@Data
public class UserIdentity {

    private Long id;
    private Long userId;
    private LoginProvider provider;
    private String identifier;

    /**
     * 密码哈希,验证码与 OAuth 为空
     */
    private String credential;

    private LocalDateTime verifiedAt;
    private LocalDateTime createdAt;

    public static UserIdentity of(long userId, LoginProvider provider, String identifier) {
        UserIdentity identity = new UserIdentity();
        identity.setUserId(userId);
        identity.setProvider(provider);
        identity.setIdentifier(identifier);
        return identity;
    }
}
