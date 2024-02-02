package com.lingyuecm.utils;

import com.lingyuecm.common.PageData;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UtilsTest {
    private static final long MOCK_USER_ID = 1L;
    /**
     * Generates UUIDs for further use
     */
    @Test
    public void generateUuid() {
        System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
    }

    @Test
    public void testContextUtils() {
        /*
        Test for user ID
         */
        assertDoesNotThrow(() -> ContextUtils.setUserId(MOCK_USER_ID));
        assertEquals(MOCK_USER_ID, ContextUtils.getUserId());

        /*
        Test for pagination data
         */
        int pageNo = 2;
        int pageSize = 10;
        PageData pageData = new PageData();
        pageData.setPageNo(pageNo);
        pageData.setPageSize(pageSize);

        assertDoesNotThrow(() -> ContextUtils.setPageData(pageData));
        assertEquals(pageSize, ContextUtils.getPageSize());
        assertEquals((pageNo - 1) * pageSize, ContextUtils.getOffset());

        assertDoesNotThrow(ContextUtils::clearContext);
        assertNull(ContextUtils.getUserId());
        assertThrows(NullPointerException.class, ContextUtils::getPageSize);
        assertThrows(NullPointerException.class, ContextUtils::getOffset);
    }
}
