package com.edda.auth.domain.model;

/**
 * 凭证校验入参
 *
 * @param provider 登录平台
 * @param identifier 平台内用户 id
 * @param credential 凭证
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public record CredentialRequest(LoginProvider provider, String identifier, String credential) {

    /**
     * 凭证不进日志
     */
    @Override
    public String toString() {
        return "CredentialRequest[provider=" + provider + ", identifier=" + identifier + ", credential=***]";
    }
}
