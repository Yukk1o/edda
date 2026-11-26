package com.edda.auth.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户账号
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
@Data
public class UserAccount {

    private Long id;
    private String nickname;
    private String avatarUrl;
    private AccountStatus status;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserAccount registerPending(String nickname) {
        return of(nickname, AccountStatus.PENDING_BIND);
    }

    public static UserAccount registerActive(String nickname) {
        return of(nickname, AccountStatus.ACTIVE);
    }

    private static UserAccount of(String nickname, AccountStatus status) {
        UserAccount account = new UserAccount();
        account.setNickname(nickname);
        account.setStatus(status);
        account.setVersion(1);
        return account;
    }

    public boolean canLogin() {
        return status != AccountStatus.DISABLED;
    }
}
