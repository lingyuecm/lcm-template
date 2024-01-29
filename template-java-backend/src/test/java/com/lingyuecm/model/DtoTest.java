package com.lingyuecm.model;

import com.lingyuecm.dto.AccessTokenDto;
import com.lingyuecm.dto.BizUserDto;
import com.lingyuecm.dto.CaptchaDto;
import com.lingyuecm.dto.LoginDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DtoTest {
    @Test
    public void testBizUserDto() {
        BizUserDto u = new BizUserDto();

        assertDoesNotThrow(() -> u.setUserId(1L));
        assertDoesNotThrow(() -> u.setLoginPwd(""));

        assertNotNull(u.getUserId());
        assertNotNull(u.getLoginPwd());
    }

    @Test
    public void testCaptchaDto() {
        CaptchaDto c = new CaptchaDto();

        assertDoesNotThrow(() -> c.setToken(""));
        assertDoesNotThrow(() -> c.setCaptchaImage(""));

        assertNotNull(c.getToken());
        assertNotNull(c.getCaptchaImage());
    }

    @Test
    public void testLoginDto() {
        LoginDto l = new LoginDto();

        assertDoesNotThrow(() -> l.setToken(""));

        assertNotNull(l.getToken());
    }

    @Test
    public void testAccessTokenDto() {
        AccessTokenDto t = new AccessTokenDto();

        assertDoesNotThrow(() -> t.setAccessToken(""));
        assertDoesNotThrow(() -> t.setJwtId(""));

        assertNotNull(t.getAccessToken());
        assertNotNull(t.getJwtId());
    }
}
