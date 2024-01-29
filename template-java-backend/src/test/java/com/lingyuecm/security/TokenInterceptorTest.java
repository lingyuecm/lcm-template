package com.lingyuecm.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingyuecm.common.LcmWebStatus;
import com.lingyuecm.dto.AccessTokenVerificationDto;
import com.lingyuecm.service.JwtService;
import com.lingyuecm.utils.ContextUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;

import static com.lingyuecm.common.Constant.HTTP_HEADER_ACCESS_TOKEN;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class TokenInterceptorTest {
    @InjectMocks
    private TokenInterceptor tokenInterceptor;
    @Mock
    private MockHttpServletRequest httpRequest;
    @Mock
    private MockHttpServletResponse httpResponse;
    @Mock
    private PrintWriter writer;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private JwtService jwtService;

    public TokenInterceptorTest() {
        assertNotNull(MockitoAnnotations.openMocks(this));
    }

    @Test
    public void preHandle_NoToken() throws IOException {
        when(this.httpRequest.getHeader(eq(HTTP_HEADER_ACCESS_TOKEN))).thenReturn(null).thenReturn("\t");
        when(this.httpResponse.getWriter()).thenReturn(this.writer);
        when(this.objectMapper.writeValueAsString(any())).thenReturn("error");
        doNothing().when(this.writer).write(anyString());

        // Token is NULL
        assertFalse(this.tokenInterceptor.preHandle(this.httpRequest, this.httpResponse, new Object()));
        // Token is blank
        assertFalse(this.tokenInterceptor.preHandle(this.httpRequest, this.httpResponse, new Object()));
    }

    @Test
    public void preHandle() throws IOException {
        when(this.httpRequest.getHeader(eq(HTTP_HEADER_ACCESS_TOKEN))).thenReturn("accessToken");
        when(this.httpResponse.getWriter()).thenReturn(this.writer);
        when(this.objectMapper.writeValueAsString(any())).thenReturn("error");
        doNothing().when(this.writer).write(anyString());

        AccessTokenVerificationDto verificationDto = new AccessTokenVerificationDto();
        verificationDto.setWebStatus(LcmWebStatus.INVALID_ACCESS_TOKEN);
        when(this.jwtService.parseAccessToken(anyString())).thenReturn(verificationDto);

        // Error parsing token
        assertFalse(this.tokenInterceptor.preHandle(this.httpRequest, this.httpResponse, new Object()));

        verificationDto.setWebStatus(LcmWebStatus.OK);
        verificationDto.setUserId(1L);
        verificationDto.setJwtId("jwtId");
        when(this.jwtService.parseAccessToken(anyString())).thenReturn(verificationDto);
        // Success
        assertTrue(this.tokenInterceptor.preHandle(this.httpRequest, this.httpResponse, new Object()));
        // The user ID will be cached on succeeding parsing the access token
        assertEquals(1L, ContextUtils.getUserId());
        assertDoesNotThrow(() -> this.tokenInterceptor.postHandle(this.httpRequest, this.httpResponse,
                new Object(), new ModelAndView()));
    }
}
