package com.edda.auth.app;

/**
 * 登录结果
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public record LoginResult(String token, String userId, boolean pendingBind) {

    public static LoginResult of(String token, long userId, boolean pendingBind) {
        return new LoginResult(token, Long.toString(userId), pendingBind);
    }
}
