package com.lingyuecm.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ContextUtils {
    static {
        USER_ID_HOLDER = new ThreadLocal<>();
    }
    private ContextUtils() {
        log.info("Utility class: {}", ContextUtils.class.getName());
    }

    private static final ThreadLocal<Long> USER_ID_HOLDER;
    public static void setUserId(Long userId) {
        USER_ID_HOLDER.set(userId);
    }

    public static Long getUserId() {
        return USER_ID_HOLDER.get();
    }

    public static void clearContext() {
        USER_ID_HOLDER.remove();
    }
}
