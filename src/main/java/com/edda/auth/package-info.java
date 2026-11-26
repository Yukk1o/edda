/**
 * 认证与账号模块
 *
 * <p>职责:账号,身份,登录,会话
 *
 * <p>允许依赖 common,不依赖任何其他领域模块
 * 它是依赖图的叶子,一旦开始调 course 就会出现环
 *
 * <p>对外只暴露 service 接口与少量 DTO,实体不出包,避免别人拿去做 join
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
package com.edda.auth;
