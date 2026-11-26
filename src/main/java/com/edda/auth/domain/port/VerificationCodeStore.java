package com.edda.auth.domain.port;

import com.edda.auth.domain.model.CodePurpose;

import java.time.Duration;

/**
 * 验证码存储器
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public interface VerificationCodeStore {

    /**
     * 保存验证码
     *
     * @param phone 手机号
     * @param purpose 用途
     * @param code 验证码
     * @param ttl 有效期
     */
    void save(String phone, CodePurpose purpose, String code, Duration ttl);

    /**
     * 消费验证码
     *
     * @param phone 手机号
     * @param purpose 用途
     * @param code 验证码
     * @return 是否消费成功
     */
    boolean consume(String phone, CodePurpose purpose, String code);

    /**
     * 增加尝试次数
     *
     * @param phone 手机号
     * @param purpose 用途
     * @return 尝试次数
     */
    int increaseAttempt(String phone, CodePurpose purpose);
}
