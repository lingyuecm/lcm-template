package com.lingyuecm.service;

import com.lingyuecm.dto.ConfPermissionDto;
import com.lingyuecm.mapper.PermissionMapper;
import com.lingyuecm.service.impl.CacheServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class CacheServiceTest {
    private static final int MOCK_PERMISSION_ID = 1;
    private static final String MOCK_HTTP_METHOD = "GET";
    private static final String MOCK_PERMISSION_URL = "/permission/url/{}";

    @InjectMocks
    private CacheServiceImpl cacheService;

    @Mock
    private PermissionMapper permissionMapper;
    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private SetOperations<String, String> setOperations;

    public CacheServiceTest() {
        assertNotNull(MockitoAnnotations.openMocks(this));
    }

    @Test
    public void cacheUserPermissions() {
        ConfPermissionDto permissionDto = this.generateMockPermission();
        when(this.permissionMapper.selectUserPermissions(anyLong())).thenReturn(null)
                .thenReturn(new ArrayList<>(){{add(permissionDto);}});
        when(this.redisTemplate.delete(anyString())).thenReturn(true);
        when(this.redisTemplate.opsForSet()).thenReturn(this.setOperations);
        when(this.setOperations.add(anyString(), any())).thenReturn(1L);

        // Empty set
        assertDoesNotThrow(() -> this.cacheService.cacheUserPermissions(1L));
        // Set with the mock permission
        assertDoesNotThrow(() -> this.cacheService.cacheUserPermissions(1L));
    }

    private ConfPermissionDto generateMockPermission() {
        ConfPermissionDto result = new ConfPermissionDto();

        result.setPermissionId(MOCK_PERMISSION_ID);
        result.setHttpMethod(MOCK_HTTP_METHOD);
        result.setPermissionUrl(MOCK_PERMISSION_URL);

        return result;
    }
}
