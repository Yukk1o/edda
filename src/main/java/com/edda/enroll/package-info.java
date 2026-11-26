/**
 * 授权与学习模块
 *
 * <p>职责:授权,学习进度,退款资格判定
 *
 * <p>允许依赖 common 与 course
 * 不依赖 order,它用订单号做弱引用,由 order 调它完成授权,它从不反向查订单
 *
 * <p>两个必须守住的点
 *
 * <ul>
 *   <li>已看秒数只增不减,否则用户可以把进度刷回去绕过退款闸门</li>
 *   <li>退款资格判定收敛在 RefundPolicy,不要在主流程里写 if</li>
 * </ul>
 *
 * <p>进度百分比必须服务端计算,前端上报的百分比一律不信
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
package com.edda.enroll;
