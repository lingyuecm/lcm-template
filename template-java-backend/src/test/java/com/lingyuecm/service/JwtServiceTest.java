package com.lingyuecm.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.lingyuecm.common.LcmWebStatus;
import com.lingyuecm.dto.AccessTokenDto;
import com.lingyuecm.dto.AccessTokenVerificationDto;
import com.lingyuecm.exception.LcmRuntimeException;
import com.lingyuecm.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtServiceTest {
    private static final String MOCK_LOGIN_CAPTCHA_ID = "captchaId";
    private static final long MOCK_USER_ID = 1L;

    @InjectMocks
    private JwtServiceImpl jwtService;

    public JwtServiceTest() {
        assertNotNull(MockitoAnnotations.openMocks(this));
    }

    @Test
    public void generateLoginToken() {
        ReflectionTestUtils.setField(this.jwtService, "loginTokenAlgorithm",
                Algorithm.HMAC256("secret"));
        String loginToken = this.jwtService.generateLoginToken(MOCK_LOGIN_CAPTCHA_ID);
        assertNotNull(loginToken);
        assertEquals(MOCK_LOGIN_CAPTCHA_ID, this.jwtService.parseLoginToken(loginToken));
        // A customized exception will be thrown on error parsing the token
        try {
            System.out.println(this.jwtService.parseLoginToken("loginToken"));
        }
        catch (LcmRuntimeException e) {
            assertEquals(LcmWebStatus.INVALID_LOGIN_TOKEN, e.getWebStatus());
            assertTrue(e.getCause() instanceof JWTVerificationException);
        }
    }

    @Test
    public void generateAccessToken() throws InterruptedException {
        ReflectionTestUtils.setField(this.jwtService, "accessTokenAlgorithm",
                Algorithm.HMAC512("secret"));
        ReflectionTestUtils.setField(this.jwtService, "accessTokenTimeout",
                30000L);// The access token expires in 30s
        AccessTokenDto tokenDto = this.jwtService.generateAccessToken(MOCK_USER_ID);
        assertNotNull(tokenDto.getAccessToken());
        AccessTokenVerificationDto verificationDto = this.jwtService.parseAccessToken(tokenDto.getAccessToken());
        assertEquals(LcmWebStatus.OK, verificationDto.getWebStatus());
        assertEquals(MOCK_USER_ID, verificationDto.getUserId());
        assertEquals(tokenDto.getJwtId(), verificationDto.getJwtId());

        ReflectionTestUtils.setField(this.jwtService, "accessTokenTimeout",
                1L);// The access token expires in 1ms
        Thread.sleep(2L);// The token expires
        tokenDto = this.jwtService.generateAccessToken(MOCK_USER_ID);
        verificationDto = this.jwtService.parseAccessToken(tokenDto.getAccessToken());
        assertEquals(LcmWebStatus.INVALID_ACCESS_TOKEN, verificationDto.getWebStatus());
    }
}
