package com.edda.auth.domain.port;

import java.util.Optional;

/**
 * 会话
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public interface SessionPort {

    /**
     * 登录
     *
     * @param userId 用户ID
     */
    void login(long userId);

    /**
     * 登出
     *
     * @throws IllegalStateException 如果用户未登录
     */
    void logout() throws IllegalStateException;

    /**
     * 获取当前用户ID
     *
     * @return 当前用户ID
     */
    Optional<Long> currentUserId();
}
