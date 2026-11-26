/**
 * 课程模块
 *
 * <p>职责:课程,课时,班期,名额,是商品域的全部内容
 *
 * <p>允许依赖 common,不依赖 order,payment,enroll,comment,coupon
 *
 * <p>关键约定:价格与名额只能由本模块修改
 * 其他模块要读价格就读快照或调接口,要扣名额就调本模块的 service,不要绕过它直接改数据
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
package com.edda.course;
