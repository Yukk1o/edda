package com.edda.auth.infra.security;

import com.edda.auth.domain.model.CodePurpose;
import com.edda.auth.domain.port.VerificationCodeStore;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryVerificationCodeStore implements VerificationCodeStore {

    private static final int MAX_ATTEMPTS = 5;
    private static final Duration
            LOCK_DURATION = Duration.ofMinutes(10);

    private record CodeKey(String phone, CodePurpose purpose) {}
    private record CodeEntry(String code, Instant
    expireAt) {}
    private record AttemptEntry(int count, Instant
    lockedUntil) {}

    private final ConcurrentHashMap<CodeKey, CodeEntry>
            codeStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<CodeKey, AttemptEntry>
            attemptStore = new ConcurrentHashMap<>();

    @Override
    public void save(String phone, CodePurpose purpose, String code, Duration ttl) {
        CodeKey key = new CodeKey(phone, purpose);
        codeStore.put(key, new CodeEntry(code,
                Instant.now().plus(ttl)));
    }

    @Override
    public boolean consume(String phone, CodePurpose purpose, String code) {
        CodeKey key = new CodeKey(phone, purpose);
        Instant now = Instant.now();
        AttemptEntry attempt = attemptStore.get(key);
        if (attempt != null && attempt.lockedUntil() != null &&
                now.isBefore(attempt.lockedUntil())) {
            return false;
        }
        AtomicBoolean consumed = new AtomicBoolean(false);
        codeStore.compute(key, (k, entry) -> {
            if (entry == null || now.isAfter(entry.expireAt())) {
                return null; // 不存在或已过期，清理掉
            }
            if (entry.code().equals(code)) {
                consumed.set(true);
                return null; // 校验成功才删除（一次性消费）
            }
            return entry; // 码不对，保留不删
        });
        if (consumed.get()) {
            attemptStore.remove(key);
        }
        return consumed.get();
    }

    @Override
    public int increaseAttempt(String phone, CodePurpose purpose) {
        CodeKey key = new CodeKey(phone, purpose);
        Instant now = Instant.now();
        AtomicInteger latestCount = new AtomicInteger();
        attemptStore.compute(key, (k, entry) -> {
            // 首次失败，或之前的锁定期已过：重新从 1 开始计数
            if (entry == null || (entry.lockedUntil() != null &&
                    !now.isBefore(entry.lockedUntil()))) {
                latestCount.set(1);
                return new AttemptEntry(1, null);
            }
            int next = entry.count() + 1;
            latestCount.set(next);
            Instant lockedUntil = next >= MAX_ATTEMPTS ?
                    now.plus(LOCK_DURATION) : null;
            return new AttemptEntry(next, lockedUntil);
        });
        return latestCount.get();
    }
}
