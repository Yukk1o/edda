package com.edda.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * 请求标识过滤器
 *
 * <p>给每个请求一个 requestId,写进 MDC 与响应头
 *
 * <p>客户端传来的 X-Request-Id 要校验格式再采用
 * 不校验等于允许别人往你的日志里注入任意内容
 * 换行会被日志系统拆成多行,伪造出假的日志条目
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdFilter extends OncePerRequestFilter {

    public static final String HEADER = "X-Request-Id";
    public static final String MDC_KEY = "requestId";

    private static final int MAX_LENGTH = 64;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestId = resolveRequestId(request);
        MDC.put(MDC_KEY, requestId);
        response.setHeader(HEADER, requestId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            // 容器线程是复用的,必须清理,否则下一个请求会带着上一个的 id
            MDC.remove(MDC_KEY);
        }
    }

    private String resolveRequestId(HttpServletRequest request) {
        String fromClient = request.getHeader(HEADER);
        if (isAcceptable(fromClient)) {
            return fromClient;
        }
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    /**
     * 只接受字母,数字,连字符,且不超过六十四位,避免日志注入
     */
    private boolean isAcceptable(String value) {
        if (value == null || value.isBlank() || value.length() > MAX_LENGTH) {
            return false;
        }
        return value.chars().allMatch(c -> Character.isLetterOrDigit(c) || c == '-');
    }
}
