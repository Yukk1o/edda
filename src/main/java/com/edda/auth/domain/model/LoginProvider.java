package com.edda.auth.domain.model;

/**
 * 登录方式
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public enum LoginProvider {

    /**
     * 密码登录
     */
    PASSWORD("password"),

    /**
     * 短信登录
     */
    SMS("sms"),

    /**
     * 邮箱登录
     */
    EMAIL("email"),

    /**
     * Discord登录
     */
    OAUTH_DISCORD("oauth_discord"),

    /**
     * 微信登录
     */
    OAUTH_WECHAT("oauth_wechat");

    private final String code;

    LoginProvider(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public static LoginProvider from(String code) {
        for (LoginProvider provider : values()) {
            if (provider.code.equals(code)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("unknown provider: " + code);
    }

    public boolean isOAuth() {
        return this == OAUTH_DISCORD || this == OAUTH_WECHAT;
    }
}
