package com.lingyuecm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingyuecm.common.LcmWebStatus;
import com.lingyuecm.dto.ConfRoleDto;
import com.lingyuecm.exception.LcmExceptionHandler;
import com.lingyuecm.request.GrantRolesRequest;
import com.lingyuecm.service.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoleControllerTest {
    private static final int MOCK_ROLE_ID = 1;
    private static final String MOCK_ROLE_NAME = "Role1";

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleService roleService;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public RoleControllerTest() {
        assertNotNull(MockitoAnnotations.openMocks(this));
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.roleController)
                .setControllerAdvice(LcmExceptionHandler.class)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void allRoles() throws Exception {
        ConfRoleDto roleDto = new ConfRoleDto();
        roleDto.setRoleId(MOCK_ROLE_ID);
        roleDto.setRoleName(MOCK_ROLE_NAME);
        when(this.roleService.getAllRoles()).thenReturn(new ArrayList<>(){{add(roleDto);}});
        this.mockMvc.perform(get("/role/allRoles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.OK.getStatusCode()))
                .andExpect(jsonPath("$.resultBody[0].roleId").value(MOCK_ROLE_ID))
                .andExpect(jsonPath("$.resultBody[0].roleName").value(MOCK_ROLE_NAME))
                .andReturn();
    }

    @Test
    public void userRoles() throws Exception {
        ConfRoleDto roleDto = new ConfRoleDto();
        roleDto.setRoleId(MOCK_ROLE_ID);
        roleDto.setRoleName(MOCK_ROLE_NAME);
        when(this.roleService.getUserRoles(anyLong())).thenReturn(new ArrayList<>(){{add(roleDto);}});
        this.mockMvc.perform(get("/role/roles/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.OK.getStatusCode()))
                .andExpect(jsonPath("$.resultBody[0].roleId").value(MOCK_ROLE_ID))
                .andExpect(jsonPath("$.resultBody[0].roleName").value(MOCK_ROLE_NAME))
                .andReturn();
    }

    @Test
    public void grantRoles() throws Exception {
        doNothing().when(this.roleService).grantRolesToUser(anyLong(), any());

        GrantRolesRequest request = new GrantRolesRequest();
        this.mockMvc.perform(post("/role/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())// [roleIds] shouldn't be NULL
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.INVALID_PARAMETER.getStatusCode()))
                .andReturn();

        request.setRoleIds(new ArrayList<>(){{add(1);add(2);}});
        this.mockMvc.perform(post("/role/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.OK.getStatusCode()))
                .andExpect(jsonPath("$.resultBody").value(0))
                .andReturn();
    }
}
