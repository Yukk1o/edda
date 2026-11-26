package com.edda.auth.infra.persistence;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

/**
 * 用户账号持久化对象
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
@TableName("app_user")
public class UserAccountPO {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String nickname;

    private String avatarUrl;

    private String status;

    @Version
    private Integer version;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField("is_deleted")
    private Integer isDeleted;
}
