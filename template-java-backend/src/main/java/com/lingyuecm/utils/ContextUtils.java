package com.lingyuecm.utils;

import com.lingyuecm.common.PageData;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ContextUtils {
    static {
        USER_ID_HOLDER = new ThreadLocal<>();
        PAGE_HOLDER = new ThreadLocal<>();
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

    /**
     * The pagination data has little to do with the business, so it could be cached for the thread so that
     * the developers don't have to pass it from controllers all the way to mappers.
     */
    private static final ThreadLocal<PageData> PAGE_HOLDER;

    public static void setPageData(PageData pageData) {
        PAGE_HOLDER.set(pageData);
    }

    public static int getPageSize() {
        return PAGE_HOLDER.get().getPageSize();
    }

    public static int getOffset() {
        return PAGE_HOLDER.get().getOffset();
    }

    public static void clearContext() {
        USER_ID_HOLDER.remove();
        PAGE_HOLDER.remove();
    }
}
