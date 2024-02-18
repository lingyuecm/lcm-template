package com.lingyuecm.service;

import com.lingyuecm.dto.ConfPermissionDto;
import com.lingyuecm.mapper.PermissionMapper;
import com.lingyuecm.service.impl.PermissionServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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

    private ConfPermissionDto generateMockPermission() {
        ConfPermissionDto result = new ConfPermissionDto();

        result.setPermissionId(MOCK_PERMISSION_ID);
        result.setHttpMethod(MOCK_HTTP_METHOD);
        result.setPermissionUrl(MOCK_PERMISSION_URL);

        return result;
    }
}
