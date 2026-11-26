package com.edda.auth.domain.model;

/**
 * 验证码用途
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public enum CodePurpose {

    /**
     * 登录
     */
    LOGIN("login"),

    /**
     * 绑定手机号
     */
    BIND_PHONE("bind_phone"),

    /**
     * 重置密码
     */
    RESET_PASSWORD("reset_password");

    private final String code;

    CodePurpose(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public static CodePurpose from(String code) {
        for (CodePurpose purpose : values()) {
            if (purpose.code.equals(code)) {
                return purpose;
            }
        }
        throw new IllegalArgumentException("unknown code purpose: " + code);
    }
}
