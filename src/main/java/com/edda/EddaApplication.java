package com.edda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 卖课站启动类
 *
 * <p>当前形态是模块化单体,见 docs/decisions/0001
 * 八个领域包在同一个进程里,靠包边界维持隔离,边界由 ArchUnit 在 CI 里守
 *
 * <p>这个类刻意保持空
 * 启动类里堆 {@code @EnableXxx} 会让系统到底用了什么能力变成需要翻启动类才知道的事
 * 能力应该由各模块自己声明
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
@SpringBootApplication
public class EddaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EddaApplication.class, args);
    }
}
