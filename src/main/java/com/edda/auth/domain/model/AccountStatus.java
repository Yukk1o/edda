package com.edda.auth.domain.model;

/**
 * 账号状态
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public enum AccountStatus {

    /**
     * 正常状态
     */
    ACTIVE("active"),

    /**
     * 第三方首次建号
     * 可登录,不可改密与退款
     */
    PENDING_BIND("pending_bind"),

    /**
     * 禁用状态
     */
    DISABLED("disabled");

    private final String code;

    AccountStatus(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public static AccountStatus from(String code) {
        for (AccountStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("unknown account status: " + code);
    }
}
