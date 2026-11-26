package com.edda.common;

import lombok.Getter;

/**
 * 错误码
 *
 * <p>编码规则:HTTP 语义 x 1000 加序号,一眼能看出责任方
 *
 * <p>三个东西的语义必须分清
 * {@code code} 给程序判断,决定能否重试,要不要跳转
 * {@code message} 给用户看,不透出内部细节
 * 堆栈给开发看
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
@Getter
public enum ErrorCode {

    // 400 参数类
    BAD_REQUEST(400001, "参数不合法"),
    COMMENT_TOO_SHORT(400002, "评论内容太短"),
    COMMENT_TOO_LONG(400003, "评论内容超长"),
    COMMENT_TOO_FREQUENT(400004, "发言过于频繁,请稍后再试"),

    // 401 403 身份与权限
    UNAUTHORIZED(401001, "请先登录"),
    FORBIDDEN(403001, "没有操作权限"),

    // 404 不存在
    USER_NOT_FOUND(404001, "用户不存在"),
    COURSE_NOT_FOUND(404002, "课程不存在"),
    PACKAGE_NOT_FOUND(404003, "班期不存在"),
    ORDER_NOT_FOUND(404004, "订单不存在"),

    // 409 状态冲突,可重试或需用户决策
    SEAT_SOLD_OUT(409001, "名额已售罄"),
    COUPON_NOT_USABLE(409002, "优惠券不可用"),
    COUPON_ALREADY_USED(409003, "优惠券已被使用"),
    ORDER_STATUS_ILLEGAL(409004, "订单状态不允许该操作"),
    ALREADY_ENROLLED(409005, "你已拥有该课程"),

    // 500 服务端
    INTERNAL_ERROR(500001, "系统繁忙,请稍后再试");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
