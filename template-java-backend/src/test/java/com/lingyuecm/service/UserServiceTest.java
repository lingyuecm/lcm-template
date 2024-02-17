package com.lingyuecm.service;

import com.lingyuecm.common.PagedList;
import com.lingyuecm.dto.AccessTokenDto;
import com.lingyuecm.dto.BizUserDto;
import com.lingyuecm.dto.CaptchaDto;
import com.lingyuecm.dto.ConfPermissionDto;
import com.lingyuecm.dto.LoginDto;
import com.lingyuecm.mapper.PermissionMapper;
import com.lingyuecm.mapper.UserMapper;
import com.lingyuecm.model.BizUser;
import com.lingyuecm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private static final String MOCK_LOGIN_TOKEN = "loginToken";
    private static final String MOCK_ACCESS_TOKEN = "accessToken";
    private static final String MOCK_CAPTCHA_ID = "captchaId";
    private static final String MOCK_CAPTCHA_1 = "captcha1";
    private static final String MOCK_CAPTCHA_2 = "captcha2";
    private static final String MOCK_PASSWORD = "password1";
    private static final long MOCK_USER_ID = 1L;
    private static final String MOCK_PHONE_NO = "123456";
    private static final String MOCK_FIRST_NAME = "FFF";
    private static final String MOCK_LAST_NAME = "LLL";
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PermissionMapper permissionMapper;
    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private SetOperations<String, String> opsForSet;
    @Mock
    private ValueOperations<String, String> valueOps;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceTest() {
        assertNotNull(MockitoAnnotations.openMocks(this));
    }

    @Test
    public void generateCaptcha() {
        when(this.redisTemplate.opsForValue()).thenReturn(this.valueOps);
        doNothing().when(this.valueOps).set(anyString(), anyString(), anyLong(), any());
        when(this.jwtService.generateLoginToken(anyString())).thenReturn(MOCK_LOGIN_TOKEN);

        CaptchaDto captcha = this.userService.generateCaptcha(300, 40);
        assertNotNull(captcha);
        assertEquals(MOCK_LOGIN_TOKEN, captcha.getToken());
    }

    @Test
    public void userLogin() {
        when(this.jwtService.parseLoginToken(anyString())).thenReturn(MOCK_CAPTCHA_ID);
        when(this.redisTemplate.hasKey(eq(MOCK_CAPTCHA_ID))).thenReturn(false).thenReturn(true);
        when(this.redisTemplate.opsForValue()).thenReturn(this.valueOps);
        when(this.valueOps.get(any())).thenReturn(MOCK_CAPTCHA_1);

        BizUserDto user = new BizUserDto();
        user.setUserId(MOCK_USER_ID);
        user.setLoginPwd(MOCK_PASSWORD);
        when(this.userMapper.selectUserCredentials(any())).thenReturn(null).thenReturn(user);
        when(this.passwordEncoder.matches(any(), anyString())).thenReturn(false).thenReturn(true);

        AccessTokenDto tokenDto = new AccessTokenDto();
        tokenDto.setAccessToken("accessToken");
        tokenDto.setJwtId("jwtId");
        when(this.jwtService.generateAccessToken(anyLong())).thenReturn(tokenDto);

        BizUser bizUser = new BizUser();
        bizUser.setPhoneNo("");
        bizUser.setLoginPwd("");
        // Captcha has expired
        assertNull(this.userService.userLogin(bizUser, MOCK_LOGIN_TOKEN, MOCK_CAPTCHA_1));
        // Captcha don't match
        assertNull(this.userService.userLogin(bizUser, MOCK_LOGIN_TOKEN, MOCK_CAPTCHA_2));
        // No user found by the phone number
        assertNull(this.userService.userLogin(bizUser, MOCK_LOGIN_TOKEN, MOCK_CAPTCHA_1));
        // Passwords don't match
        assertNull(this.userService.userLogin(bizUser, MOCK_LOGIN_TOKEN, MOCK_CAPTCHA_1));

        ConfPermissionDto permissionDto = new ConfPermissionDto();
        permissionDto.setHttpMethod("GET");
        permissionDto.setPermissionUrl("/permission/url");
        when(this.permissionMapper.selectUserPermissions(anyLong())).thenReturn(null)
                .thenReturn(new ArrayList<>(){{add(permissionDto);}});
        when(this.redisTemplate.delete(anyString())).thenReturn(true);
        when(this.redisTemplate.opsForSet()).thenReturn(this.opsForSet);
        when(this.opsForSet.add(anyString(), any())).thenReturn(1L);
        // Success, no permissions
        LoginDto successDto = this.userService.userLogin(bizUser, MOCK_LOGIN_TOKEN, MOCK_CAPTCHA_1);
        assertNotNull(successDto);
        assertEquals(MOCK_ACCESS_TOKEN, successDto.getToken());

        // Success, has permissions
        successDto = this.userService.userLogin(bizUser, MOCK_LOGIN_TOKEN, MOCK_CAPTCHA_1);
        assertNotNull(successDto);
        assertEquals(MOCK_ACCESS_TOKEN, successDto.getToken());
    }

    @Test
    public void getMetadata() {
        BizUserDto userDto = new BizUserDto();
        userDto.setFirstName(MOCK_FIRST_NAME);
        userDto.setLastName(MOCK_LAST_NAME);
        when(this.userMapper.selectMetadata()).thenReturn(userDto);

        BizUserDto result = this.userService.getMetadata();
        assertEquals(MOCK_FIRST_NAME, result.getFirstName());
        assertEquals(MOCK_LAST_NAME, result.getLastName());
    }

    @Test
    public void getUsers() {
        BizUserDto userDto = new BizUserDto();
        userDto.setPhoneNo(MOCK_PHONE_NO);
        userDto.setFirstName(MOCK_FIRST_NAME);
        userDto.setLastName(MOCK_LAST_NAME);
        when(this.userMapper.manageUsers(anyString())).thenReturn(null)
                .thenReturn(new ArrayList<>())
                .thenReturn(new ArrayList<>(){{add(userDto);}});
        // NULL
        PagedList<BizUserDto> result = this.userService.getUsers("criteria");
        this.getUsersAssertionsEmpty(result);
        // Empty list
        result = this.userService.getUsers("criteria");
        this.getUsersAssertionsEmpty(result);
        // Non-Empty list
        long totalCount = 100L;
        when(this.userMapper.selectUserCount(anyString())).thenReturn(totalCount);
        result = this.userService.getUsers("criteria");
        assertNotNull(result);
        assertEquals(totalCount, result.getTotalCount());
        assertNotNull(result.getDataList());
        assertEquals(1, result.getDataList().size());
    }
    private void getUsersAssertionsEmpty(PagedList<BizUserDto> result) {
        assertNotNull(result);
        assertEquals(0L, result.getTotalCount());
        assertNotNull(result.getDataList());
        assertEquals(0, result.getDataList().size());
    }
}
