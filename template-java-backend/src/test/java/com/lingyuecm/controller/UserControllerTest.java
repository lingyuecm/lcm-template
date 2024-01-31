package com.lingyuecm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingyuecm.common.LcmWebStatus;
import com.lingyuecm.dto.BizUserDto;
import com.lingyuecm.dto.CaptchaDto;
import com.lingyuecm.dto.LoginDto;
import com.lingyuecm.exception.LcmExceptionHandler;
import com.lingyuecm.request.LoginRequest;
import com.lingyuecm.request.RefreshCaptchaRequest;
import com.lingyuecm.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {
    private static final String MOCK_TOKEN = "token";
    private static final String MOCK_CAPTCHA = "captchaBase64";
    private static final String MOCK_FIRST_NAME = "FFF";
    private static final String MOCK_LAST_NAME = "LLL";
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public UserControllerTest() {
        assertNotNull(MockitoAnnotations.openMocks(this));
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.userController)
                .setControllerAdvice(LcmExceptionHandler.class).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void refreshCaptcha() throws Exception {
        RefreshCaptchaRequest request = new RefreshCaptchaRequest();
        // NULL
        this.mockMvc.perform(post("/user/captcha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.INVALID_PARAMETER.getStatusCode()))
                .andReturn();

        // Exceeds the upper bound
        request.setCaptchaWidth(301);
        request.setCaptchaHeight(301);
        this.mockMvc.perform(post("/user/captcha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.INVALID_PARAMETER.getStatusCode()))
                .andReturn();

        // Exceeds the lower bound
        request.setCaptchaWidth(29);
        request.setCaptchaHeight(29);
        this.mockMvc.perform(post("/user/captcha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.INVALID_PARAMETER.getStatusCode()))
                .andReturn();

        // Success
        request.setCaptchaWidth(300);
        request.setCaptchaHeight(40);
        CaptchaDto captcha = new CaptchaDto();
        captcha.setToken(MOCK_TOKEN);
        captcha.setCaptchaImage(MOCK_CAPTCHA);
        when(this.userService.generateCaptcha(anyInt(), anyInt())).thenReturn(captcha);
        this.mockMvc.perform(post("/user/captcha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.OK.getStatusCode()))
                .andExpect(jsonPath("$.resultBody.token").value(MOCK_TOKEN))
                .andExpect(jsonPath("$.resultBody.captchaImage").value(MOCK_CAPTCHA))
                .andReturn();
    }

    @Test
    public void userLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        // NULL
        this.mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.INVALID_PARAMETER.getStatusCode()))
                .andReturn();

        request.setPhoneNo("");
        request.setPassword("");
        request.setCaptcha("");
        request.setToken("");

        LoginDto loginDto = new LoginDto();
        loginDto.setToken(MOCK_TOKEN);
        when(this.userService.userLogin(any(), anyString(), anyString())).thenReturn(null).thenReturn(loginDto);
        // Failed to log in
        this.mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.FAILED_TO_LOGIN.getStatusCode()))
                .andReturn();

        // Success
        this.mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.OK.getStatusCode()))
                .andExpect(jsonPath("$.resultBody.token").value(MOCK_TOKEN))
                .andReturn();
    }

    @Test
    public void metadata() throws Exception {
        BizUserDto userDto = new BizUserDto();
        userDto.setFirstName(MOCK_FIRST_NAME);
        userDto.setLastName(MOCK_LAST_NAME);
        when(this.userService.getMetadata()).thenReturn(userDto);

        this.mockMvc.perform(get("/user/metadata"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.OK.getStatusCode()))
                .andExpect(jsonPath("$.resultBody.firstName").value(MOCK_FIRST_NAME))
                .andExpect(jsonPath("$.resultBody.lastName").value(MOCK_LAST_NAME))
                .andReturn();
    }
}
