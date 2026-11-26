package com.edda.auth.domain.model;

/**
 * 外部主体
 *
 * @param provider 登录平台
 * @param externalId 平台内用户 id
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public record ExternalPrincipal(LoginProvider provider, String externalId) {
}
