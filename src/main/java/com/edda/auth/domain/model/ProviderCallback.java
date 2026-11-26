package com.edda.auth.domain.model;

/**
 * OAuth 回调入参
 *
 * @param state 状态
 * @param authorizationCode 授权码
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public record ProviderCallback(String state, String authorizationCode) {
}
