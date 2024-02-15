package com.lingyuecm.service;

import com.lingyuecm.common.PagedList;
import com.lingyuecm.dto.ConfRoleDto;
import com.lingyuecm.mapper.RoleMapper;
import com.lingyuecm.service.impl.RoleServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class RoleServiceTest {
    private static final int MOCK_ROLE_ID = 1;
    private static final String MOCK_ROLE_NAME = "Role1";
    private static final int MOCK_STATUS = 1;
    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleMapper roleMapper;

    public RoleServiceTest() {
        assertNotNull(MockitoAnnotations.openMocks(this));
    }

    @Test
    public void getAllRoles() {
        ConfRoleDto roleDto = new ConfRoleDto();
        roleDto.setRoleId(MOCK_ROLE_ID);
        roleDto.setRoleName(MOCK_ROLE_NAME);
        when(this.roleMapper.selectAllRoles()).thenReturn(null)
                .thenReturn(new ArrayList<>(){{add(roleDto);}});

        List<ConfRoleDto> roles = this.roleService.getAllRoles();
        assertNotNull(roles);
        assertEquals(0, roles.size());

        roles = this.roleService.getAllRoles();
        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals(MOCK_ROLE_ID, roles.get(0).getRoleId());
        assertEquals(MOCK_ROLE_NAME, roles.get(0).getRoleName());
    }

    @Test
    public void getUserRoles() {
        ConfRoleDto roleDto = new ConfRoleDto();
        roleDto.setRoleId(MOCK_ROLE_ID);
        roleDto.setRoleName(MOCK_ROLE_NAME);
        when(this.roleMapper.selectUserRoles(anyLong())).thenReturn(null)
                .thenReturn(new ArrayList<>(){{add(roleDto);}});

        List<ConfRoleDto> roles = this.roleService.getUserRoles(1L);
        assertNotNull(roles);
        assertEquals(0, roles.size());

        roles = this.roleService.getUserRoles(1L);
        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals(MOCK_ROLE_ID, roles.get(0).getRoleId());
        assertEquals(MOCK_ROLE_NAME, roles.get(0).getRoleName());
    }

    @Test
    public void grantRolesToUser() {
        doNothing().when(this.roleMapper).deleteUserRoles(anyLong());
        doNothing().when(this.roleMapper).insertUserRoles(anyLong(), any());

        // Grant no roles
        assertDoesNotThrow(() -> this.roleService.grantRolesToUser(1L, new ArrayList<>()));
        // Grant some roles
        assertDoesNotThrow(() -> this.roleService.grantRolesToUser(1L, new ArrayList<>(){{add(1);add(2);}}));
    }

    @Test
    public void getRoles() {
        ConfRoleDto roleDto = new ConfRoleDto();
        roleDto.setRoleId(MOCK_ROLE_ID);
        roleDto.setRoleName(MOCK_ROLE_NAME);
        roleDto.setStatus(MOCK_STATUS);
        when(this.roleMapper.manageRoles(anyString()))
                .thenReturn(null)
                .thenReturn(new ArrayList<>(){{add(roleDto);}});
        PagedList<ConfRoleDto> result = this.roleService.getRoles("criteria");
        assertNotNull(result);
        assertEquals(0L, result.getTotalCount());
        assertNotNull(result.getDataList());
        assertEquals(0, result.getDataList().size());

        long totalCount = 100L;
        when(this.roleMapper.selectRoleCount(anyString())).thenReturn(totalCount);
        result = this.roleService.getRoles("criteria");
        assertNotNull(result);
        assertEquals(totalCount, result.getTotalCount());
        assertNotNull(result.getDataList());
        assertEquals(1, result.getDataList().size());
    }
}
