package com.edda.auth.domain.model;

/**
 * 认证主体
 *
 * @param userId 用户 id
 * @param provider 登录平台
 * @param identifier 平台内用户 id
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public record AuthSubject(long userId, LoginProvider provider, String identifier) {
}
