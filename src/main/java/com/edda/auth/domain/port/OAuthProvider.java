package com.edda.auth.domain.port;

import com.edda.auth.domain.model.AuthorizationIntent;
import com.edda.auth.domain.model.ExternalPrincipal;
import com.edda.auth.domain.model.LoginProvider;
import com.edda.auth.domain.model.ProviderCallback;

import java.net.URI;

/**
 * OAuth提供者
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public interface OAuthProvider {

    /**
     * 支持的登录提供者
     *
     * @return 登录提供者
     */
    LoginProvider provider();

    /**
     * 获取授权地址
     *
     * @param intent 授权意图
     * @param state  状态
     * @return 授权地址
     */
    URI authorizationUri(AuthorizationIntent intent, String state);

    /**
     * 交换身份
     *
     * @param callback 回调
     * @return 外部主体
     */
    ExternalPrincipal exchangeIdentity(ProviderCallback callback);

}
