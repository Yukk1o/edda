-- V1: 认证域初始表

-- =====================================================
-- 用户账号表
-- =====================================================
CREATE TABLE app_user
(
    id         bigint      NOT NULL COMMENT '用户ID',
    nickname   varchar(64) NOT NULL COMMENT '昵称',
    avatar_url varchar(512)         COMMENT '头像地址',
    status     varchar(16) NOT NULL DEFAULT 'active' COMMENT '账号状态,见 chk_app_user_status',
    created_at datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    updated_at datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    version    integer     NOT NULL DEFAULT 1 COMMENT '乐观锁版本',
    is_deleted smallint    NOT NULL DEFAULT 0 COMMENT '是否删除,0-否 1-是',
    PRIMARY KEY (id),
    CONSTRAINT chk_app_user_status CHECK (status IN ('active', 'pending_bind', 'disabled'))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '用户账号表 - 存储账号聚合根';

CREATE INDEX idx_app_user_status ON app_user (status);
CREATE INDEX idx_app_user_created_at ON app_user (created_at DESC);

-- =====================================================
-- 用户身份表
-- 一个账号可以有多种身份,加一种登录方式只插一行数据,不改表结构
-- 密码属于身份而不是账号:换登录方式就是换一行,不是给账号加一列
-- =====================================================
CREATE TABLE user_identity
(
    id          bigint       NOT NULL COMMENT '身份ID',
    user_id     bigint       NOT NULL COMMENT '用户ID(app_user.id)',
    provider    varchar(32)  NOT NULL COMMENT '登录方式:password/sms/oauth_discord/oauth_wechat',
    identifier  varchar(128) NOT NULL COMMENT '身份标识:手机号/用户名/三方用户id',
    credential  varchar(255)          COMMENT '凭证:密码哈希,验证码与 OAuth 为空',
    verified_at datetime(3)           COMMENT '校验通过时间',
    created_at  datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    updated_at  datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (id),
    CONSTRAINT uk_user_identity_provider_identifier UNIQUE (provider, identifier),
    CONSTRAINT chk_user_identity_provider CHECK (provider IN ('password', 'sms', 'oauth_discord', 'oauth_wechat'))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '用户身份表 - 一个账号的多种登录方式';

CREATE INDEX idx_user_identity_user_id ON user_identity (user_id);
CREATE INDEX idx_user_identity_provider ON user_identity (provider);
