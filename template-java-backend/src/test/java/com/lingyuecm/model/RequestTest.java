package com.lingyuecm.model;

import com.lingyuecm.request.LoginRequest;
import com.lingyuecm.request.RefreshCaptchaRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RequestTest {
    @Test
    public void testLoginRequest() {
        LoginRequest r = new LoginRequest();

        assertDoesNotThrow(() -> r.setPhoneNo(""));
        assertDoesNotThrow(() -> r.setPassword(""));
        assertDoesNotThrow(() -> r.setCaptcha(""));
        assertDoesNotThrow(() -> r.setToken(""));

        assertNotNull(r.getPhoneNo());
        assertNotNull(r.getPassword());
        assertNotNull(r.getCaptcha());
        assertNotNull(r.getToken());
    }

    @Test
    public void testRefreshCaptchaRequest() {
        RefreshCaptchaRequest r = new RefreshCaptchaRequest();

        assertDoesNotThrow(() -> r.setCaptchaWidth(300));
        assertDoesNotThrow(() -> r.setCaptchaHeight(40));

        assertNotNull(r.getCaptchaWidth());
        assertNotNull(r.getCaptchaHeight());
    }
}
