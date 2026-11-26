package com.edda.auth.domain.port;

import com.edda.auth.domain.model.LoginProvider;
import com.edda.auth.domain.model.UserIdentity;

import java.util.List;
import java.util.Optional;

/**
 * 用户身份仓储
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
public interface UserIdentityRepository {

    /**
     * 根据登录提供者和标识符查找用户身份
     *
     * @param provider   登录提供者
     * @param identifier 标识符
     * @return 用户身份
     */
    Optional<UserIdentity> find(LoginProvider provider, String identifier);

    /**
     * 根据用户ID查找用户身份列表
     *
     * @param userId 用户ID
     * @return 用户身份列表
     */
    List<UserIdentity> listByUserId(long userId);

    /**
     * 保存用户身份
     *
     * @param identity 用户身份
     * @return 保存后的用户身份
     */
    UserIdentity save(UserIdentity identity);

    /**
     * 根据ID删除用户身份
     *
     * @param id 用户身份ID
     */
    void deleteById(long id);
}
