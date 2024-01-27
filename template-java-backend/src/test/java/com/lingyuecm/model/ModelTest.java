package com.lingyuecm.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ModelTest {
    @Test
    public void testBizUser() {
        BizUser u = new BizUser();

        assertDoesNotThrow(() -> u.setUserId(1L));
        assertDoesNotThrow(() -> u.setPhoneNo(""));
        assertDoesNotThrow(() -> u.setLoginPwd(""));
        assertDoesNotThrow(() -> u.setRealName(""));
        assertDoesNotThrow(() -> u.setEnabled(1));
        assertDoesNotThrow(() -> u.setCreatedBy(1L));
        assertDoesNotThrow(() -> u.setTimeCreated(new Date()));
        assertDoesNotThrow(() -> u.setUpdatedBy(1L));
        assertDoesNotThrow(() -> u.setTimeUpdated(new Date()));

        assertNotNull(u.getUserId());
        assertNotNull(u.getPhoneNo());
        assertNotNull(u.getLoginPwd());
        assertNotNull(u.getRealName());
        assertNotNull(u.getEnabled());
        assertNotNull(u.getCreatedBy());
        assertNotNull(u.getTimeCreated());
        assertNotNull(u.getUpdatedBy());
        assertNotNull(u.getTimeUpdated());
    }
}
