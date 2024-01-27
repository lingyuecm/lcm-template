package com.lingyuecm.exception;

import com.lingyuecm.common.LcmWebResult;
import com.lingyuecm.common.LcmWebStatus;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExceptionTest {
    @InjectMocks
    private LcmExceptionHandler exceptionHandler;

    public ExceptionTest() {
        assertNotNull(MockitoAnnotations.openMocks(this));
    }

    @Test
    public void handleLcmRuntimeException() {
        LcmWebResult<Boolean> result = this.exceptionHandler.handleLcmRuntimeException(
                new LcmRuntimeException(LcmWebStatus.FAILED_TO_LOGIN, new NullPointerException("NULL")));
        assertNotNull(result);
        assertEquals(LcmWebStatus.FAILED_TO_LOGIN.getStatusCode(), result.getResultCode());
    }

    @Test
    public void handleException() {
        LcmWebResult<Boolean> result = this.exceptionHandler.handleException(new NullPointerException("NULL"));
        assertNotNull(result);
        assertEquals(LcmWebStatus.INTERNAL_SERVER_ERROR.getStatusCode(), result.getResultCode());
    }
}
