package com.edda.auth.domain.port;

import com.edda.auth.domain.model.UserAccount;

import java.util.Optional;

/**
 * 用户账号仓储
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public interface UserAccountRepository {

    /**
     * 根据ID查找用户账号
     *
     * @param id 用户账号ID
     * @return 用户账号
     */
    Optional<UserAccount> findById(long id);

    /**
     * 保存用户账号
     *
     * @param account 用户账号
     * @return 保存后的用户账号
     */
    UserAccount save(UserAccount account);
}
