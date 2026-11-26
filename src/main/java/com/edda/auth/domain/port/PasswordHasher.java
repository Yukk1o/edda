package com.edda.auth.domain.port;

/**
 * 密码哈希器
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public interface PasswordHasher {

    /**
     * 编码密码
     *
     * @param rawPassword 原始密码
     * @return 编码后的密码
     */
    String encode(CharSequence rawPassword);

    /**
     * 匹配密码
     *
     * @param rawPassword 原始密码
     * @param encoded 编码后的密码
     * @return 是否匹配
     */
    boolean matches(CharSequence rawPassword, String encoded);

    /**
     * 是否需要升级
     *
     * @param encoded 编码后的密码
     * @return 是否需要升级
     */
    boolean needsUpgrade(String encoded);
}
