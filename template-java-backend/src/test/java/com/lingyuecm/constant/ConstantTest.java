package com.lingyuecm.constant;

import com.lingyuecm.common.LcmWebResult;
import com.lingyuecm.common.LcmWebStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ConstantTest {
    @Test
    public void testWebResult() {
        LcmWebResult<Integer> success = LcmWebResult.success(0);
        assertNotNull(success);
        assertEquals(LcmWebStatus.OK.getStatusCode(), success.getResultCode());
        assertNotNull(success.getResultMessage());
        assertNotNull(success.getResultBody());

        LcmWebResult<Integer> failure = LcmWebResult.failure(LcmWebStatus.FAILED_TO_LOGIN);
        assertNotNull(failure);
        assertEquals(LcmWebStatus.FAILED_TO_LOGIN.getStatusCode(), failure.getResultCode());
        assertNotNull(failure.getResultMessage());
        assertNull(failure.getResultBody());
    }

    @Test
    public void testWebStatus() {
        for (LcmWebStatus s : LcmWebStatus.values()) {
            assertDoesNotThrow(s::getStatusCode);
            assertNotNull(s.getStatusMessage());
        }
    }
}
