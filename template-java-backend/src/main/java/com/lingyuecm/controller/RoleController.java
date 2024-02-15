package com.lingyuecm.controller;

import com.lingyuecm.common.LcmWebResult;
import com.lingyuecm.common.PageData;
import com.lingyuecm.common.PagedList;
import com.lingyuecm.dto.ConfRoleDto;
import com.lingyuecm.request.GetRolesRequest;
import com.lingyuecm.request.GrantRolesRequest;
import com.lingyuecm.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    /**
     * Gets all available roles of the system
     */
    @GetMapping("/allRoles")
    public LcmWebResult<List<ConfRoleDto>> allRoles() {
        List<ConfRoleDto> result = this.roleService.getAllRoles();
        return LcmWebResult.success(result);
    }

    /**
     * Gets all roles granted to the user
     */
    @GetMapping("/roles/{userId}")
    public LcmWebResult<List<ConfRoleDto>> userRoles(@PathVariable Long userId) {
        List<ConfRoleDto> result = this.roleService.getUserRoles(userId);
        return LcmWebResult.success(result);
    }

    /**
     * Grants roles for the user
     */
    @PostMapping("/roles/{userId}")
    public LcmWebResult<Integer> userRoles(@PathVariable Long userId,
                                           @RequestBody @Validated GrantRolesRequest request) {
        this.roleService.grantRolesToUser(userId, request.getRoleIds());
        return LcmWebResult.success(0);
    }

    /**
     * Gets the role list for the admin
     */
    @GetMapping("/roles")
    public LcmWebResult<PagedList<ConfRoleDto>> roles(GetRolesRequest request, @SuppressWarnings("unused") PageData pageData) {
        PagedList<ConfRoleDto> result = this.roleService.getRoles(request.getCriteria());
        return LcmWebResult.success(result);
    }
}
