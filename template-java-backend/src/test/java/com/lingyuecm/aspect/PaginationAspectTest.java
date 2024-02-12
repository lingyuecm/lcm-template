package com.lingyuecm.aspect;

import com.lingyuecm.common.PageData;
import com.lingyuecm.utils.ContextUtils;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class PaginationAspectTest {
    @InjectMocks
    private PaginationAspect paginationAspect;

    @Mock
    private JoinPoint joinPoint;

    public PaginationAspectTest() {
        assertNotNull(MockitoAnnotations.openMocks(this));
    }

    @Test
    public void beforePagination() {
        Object[] arguments = new Object[2];
        PageData pageData = new PageData();
        int pageNo = 2;
        int pageSize = 10;
        pageData.setPageNo(pageNo);
        pageData.setPageSize(pageSize);
        arguments[1] = pageData;
        when(this.joinPoint.getArgs()).thenReturn(arguments);

        assertDoesNotThrow(() -> this.paginationAspect.beforePagination(this.joinPoint));
        assertEquals(pageSize, ContextUtils.getPageSize());
        assertEquals((pageNo - 1) * pageSize, ContextUtils.getOffset());
    }
}
