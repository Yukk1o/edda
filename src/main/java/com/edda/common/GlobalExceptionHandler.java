package com.edda.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * <p>把四类失败映射成同一套 Result
 *
 * <ul>
 *   <li>业务异常:原样透出 code,warn 日志</li>
 *   <li>参数校验:400001,取第一条字段错误</li>
 *   <li>缺请求头或请求体解析失败:400001</li>
 *   <li>其他:500001,error 日志加全堆栈</li>
 * </ul>
 *
 * <p>注意错误信息不要拼 SQL,表名,类名
 * 前端拿到的 message 只用来展示,内部细节只进日志
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public Result<Void> handleBiz(BizException e) {
        log.warn("业务失败: code={}, message={}", e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<Void> handleValidation(BindException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse(ErrorCode.BAD_REQUEST.getMessage());
        log.warn("参数校验失败: {}", message);
        return Result.error(ErrorCode.BAD_REQUEST.getCode(), message);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingRequestHeaderException.class})
    public Result<Void> handleBadRequest(Exception e) {
        log.warn("请求不合法: {}", e.getMessage());
        return Result.error(ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleUnknown(Exception e) {
        log.error("未预期异常", e);
        return Result.error(ErrorCode.INTERNAL_ERROR);
    }
}
