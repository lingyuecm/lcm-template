package com.lingyuecm.service;

import com.lingyuecm.common.Constant;
import com.lingyuecm.common.PagedList;
import com.lingyuecm.dto.ConfPermissionDto;
import com.lingyuecm.mapper.PermissionMapper;
import com.lingyuecm.model.ConfPermission;
import com.lingyuecm.service.impl.PermissionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class PermissionServiceTest {
    private static final int MOCK_PERMISSION_ID = 1;
    private static final String MOCK_HTTP_METHOD = "GET";
    private static final String MOCK_PERMISSION_URL = "/permission/url/{}";

    @InjectMocks
    private PermissionServiceImpl permissionService;

    @Mock
    private PermissionMapper permissionMapper;
    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private CacheService cacheService;

    public PermissionServiceTest() {
        assertNotNull(MockitoAnnotations.openMocks(this));
    }

    @Test
    public void getAllPermissions() {
        ConfPermissionDto permissionDto = this.generateMockPermission();
        when(this.permissionMapper.selectAllPermissions()).thenReturn(null)
                .thenReturn(new ArrayList<>(){{add(permissionDto);}});

        // NULL
        List<ConfPermissionDto> permissions = this.permissionService.getAllPermissions();
        assertNotNull(permissions);
        assertEquals(0, permissions.size());

        // List with one permission
        permissions = this.permissionService.getAllPermissions();
        assertNotNull(permissions);
        assertEquals(1, permissions.size());
        assertEquals(MOCK_PERMISSION_ID, permissions.get(0).getPermissionId());
        assertEquals(MOCK_HTTP_METHOD, permissions.get(0).getHttpMethod());
        assertEquals(MOCK_PERMISSION_URL, permissions.get(0).getPermissionUrl());
    }

    @Test
    public void getRolePermissions() {
        ConfPermissionDto permissionDto = this.generateMockPermission();
        when(this.permissionMapper.selectRolePermissions(anyInt())).thenReturn(null)
                .thenReturn(new ArrayList<>(){{add(permissionDto);}});

        // NULL
        List<ConfPermissionDto> permissions = this.permissionService.getRolePermissions(1);
        assertNotNull(permissions);
        assertEquals(0, permissions.size());

        // List with one permission
        permissions = this.permissionService.getRolePermissions(1);
        assertNotNull(permissions);
        assertEquals(1, permissions.size());
        assertEquals(MOCK_PERMISSION_ID, permissions.get(0).getPermissionId());
        assertEquals(MOCK_HTTP_METHOD, permissions.get(0).getHttpMethod());
        assertEquals(MOCK_PERMISSION_URL, permissions.get(0).getPermissionUrl());
    }

    @Test
    public void grantPermissionsToRole() {
        doNothing().when(this.permissionMapper).deleteRolePermissions(anyInt());
        doNothing().when(this.permissionMapper).insertRolePermissions(anyInt(), any());

        assertDoesNotThrow(() -> this.permissionService.grantPermissionsToRole(1, new ArrayList<>()));
        assertDoesNotThrow(() -> this.permissionService.grantPermissionsToRole(1,
                new ArrayList<>(){{add(MOCK_PERMISSION_ID);}}));
    }

    @Test
    public void getPermissions() {
        ConfPermissionDto permissionDto = this.generateMockPermission();
        when(this.permissionMapper.managePermissions(any())).thenReturn(null)
                .thenReturn(new ArrayList<>(){{add(permissionDto);}});
        long totalCount = 100L;
        when(this.permissionMapper.selectPermissionCount(any())).thenReturn(totalCount);

        // NULL
        PagedList<ConfPermissionDto> result = this.permissionService.getPermissions(new ConfPermission());
        assertEquals(totalCount, result.getTotalCount());
        assertNotNull(result.getDataList());
        assertEquals(0, result.getDataList().size());

        // List with one item
        result = this.permissionService.getPermissions(new ConfPermission());
        assertEquals(totalCount, result.getTotalCount());
        assertNotNull(result.getDataList());
        assertEquals(1, result.getDataList().size());
        assertEquals(MOCK_PERMISSION_ID, result.getDataList().get(0).getPermissionId());
        assertEquals(MOCK_HTTP_METHOD, result.getDataList().get(0).getHttpMethod());
        assertEquals(MOCK_PERMISSION_URL, result.getDataList().get(0).getPermissionUrl());
    }

    @Test
    public void refreshPermissionCache() {
        when(this.redisTemplate.keys(anyString())).thenReturn(null)
                .thenReturn(new HashSet<>(){{add(Constant.REDIS_PREFIX_USER_PERMISSION + 1);}});
        doNothing().when(this.cacheService).cacheUserPermissions(anyLong());

        // NULL
        assertDoesNotThrow(() -> this.permissionService.refreshPermissionCache());
        // Cache refreshed
        assertDoesNotThrow(() -> this.permissionService.refreshPermissionCache());
    }

    private ConfPermissionDto generateMockPermission() {
        ConfPermissionDto result = new ConfPermissionDto();

        result.setPermissionId(MOCK_PERMISSION_ID);
        result.setHttpMethod(MOCK_HTTP_METHOD);
        result.setPermissionUrl(MOCK_PERMISSION_URL);

        return result;
    }
}
