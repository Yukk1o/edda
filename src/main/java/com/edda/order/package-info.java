/**
 * 订单模块
 *
 * <p>职责:下单编排,订单状态机,超时关单,退款申请
 *
 * <p>允许依赖 common,course,stock,coupon,enroll
 * 它是编排层,可以调别人,别人不许调它
 *
 * <p>三条铁律
 *
 * <ol>
 *   <li>状态迁移只能走状态机,禁止在别处直接改状态</li>
 *   <li>写操作尽量往后放,只读校验,只读试算,冻名额,锁券,落单</li>
 *   <li>非法迁移抛异常,不许静默忽略</li>
 * </ol>
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
package com.edda.order;
