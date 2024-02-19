package com.lingyuecm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingyuecm.common.LcmWebStatus;
import com.lingyuecm.common.PagedList;
import com.lingyuecm.dto.ConfPermissionDto;
import com.lingyuecm.exception.LcmExceptionHandler;
import com.lingyuecm.request.GrantPermissionsRequest;
import com.lingyuecm.service.PermissionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PermissionControllerTest {
    private static final int MOCK_PERMISSION_ID = 1;
    private static final String MOCK_HTTP_METHOD = "GET";
    private static final String MOCK_PERMISSION_URL = "/permission/url/{}";

    @InjectMocks
    private PermissionController permissionController;

    @Mock
    private PermissionService permissionService;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public PermissionControllerTest() {
        assertNotNull(MockitoAnnotations.openMocks(this));

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.permissionController)
                .setControllerAdvice(LcmExceptionHandler.class)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void allPermissions() throws Exception {
        ConfPermissionDto permissionDto = this.generateMockPermission();

        when(this.permissionService.getAllPermissions()).thenReturn(new ArrayList<>(){{add(permissionDto);}});
        this.mockMvc.perform(get("/permission/permissions/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.OK.getStatusCode()))
                .andExpect(jsonPath("$.resultBody[0].permissionId").value(MOCK_PERMISSION_ID))
                .andExpect(jsonPath("$.resultBody[0].httpMethod").value(MOCK_HTTP_METHOD))
                .andExpect(jsonPath("$.resultBody[0].permissionUrl").value(MOCK_PERMISSION_URL))
                .andReturn();
    }

    @Test
    public void rolePermissions() throws Exception {
        ConfPermissionDto permissionDto = this.generateMockPermission();

        when(this.permissionService.getRolePermissions(anyInt())).thenReturn(new ArrayList<>(){{add(permissionDto);}});
        this.mockMvc.perform(get("/permission/permissions/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.OK.getStatusCode()))
                .andExpect(jsonPath("$.resultBody[0].permissionId").value(MOCK_PERMISSION_ID))
                .andExpect(jsonPath("$.resultBody[0].httpMethod").value(MOCK_HTTP_METHOD))
                .andExpect(jsonPath("$.resultBody[0].permissionUrl").value(MOCK_PERMISSION_URL))
                .andReturn();
    }

    @Test
    public void grantPermissions() throws Exception {
        doNothing().when(this.permissionService).grantPermissionsToRole(anyInt(), any());

        GrantPermissionsRequest r = new GrantPermissionsRequest();
        r.setPermissionIds(new ArrayList<>(){{add(MOCK_PERMISSION_ID);}});
        this.mockMvc.perform(post("/permission/permissions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(r)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.OK.getStatusCode()))
                .andReturn();
    }

    @Test
    public void getPermissions() throws Exception {
        ConfPermissionDto permissionDto = this.generateMockPermission();
        long totalCount = 100L;
        when(this.permissionService.getPermissions(any()))
                .thenReturn(PagedList.paginated(totalCount, new ArrayList<>(){{add(permissionDto);}}));
        this.mockMvc.perform(get("/permission/permissions?httpMethod=GET&permissionUrl=%2Fpermission%2Furl&pageNo=1&pageSize=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(LcmWebStatus.OK.getStatusCode()))
                .andExpect(jsonPath("$.resultBody.totalCount").value(totalCount))
                .andExpect(jsonPath("$.resultBody.dataList[0].permissionId").value(MOCK_PERMISSION_ID))
                .andExpect(jsonPath("$.resultBody.dataList[0].httpMethod").value(MOCK_HTTP_METHOD))
                .andExpect(jsonPath("$.resultBody.dataList[0].permissionUrl").value(MOCK_PERMISSION_URL))
                .andReturn();
    }

    private ConfPermissionDto generateMockPermission() {
        ConfPermissionDto result = new ConfPermissionDto();

        result.setPermissionId(MOCK_PERMISSION_ID);
        result.setHttpMethod(MOCK_HTTP_METHOD);
        result.setPermissionUrl(MOCK_PERMISSION_URL);

        return result;
    }
}
