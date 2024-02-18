package com.lingyuecm.controller;

import com.lingyuecm.common.LcmWebResult;
import com.lingyuecm.dto.ConfPermissionDto;
import com.lingyuecm.request.GrantPermissionsRequest;
import com.lingyuecm.service.PermissionService;
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
@RequestMapping("/permission")
public class PermissionController {
    @Resource
    private PermissionService permissionService;

    /**
     * Gets all available permissions of the system
     */
    @GetMapping("/permissions/all")
    public LcmWebResult<List<ConfPermissionDto>> allPermissions() {
        List<ConfPermissionDto> result = this.permissionService.getAllPermissions();
        return LcmWebResult.success(result);
    }

    /**
     * Gets all permissions granted to the role
     */
    @GetMapping("/permissions/{roleId}")
    public LcmWebResult<List<ConfPermissionDto>> rolePermissions(@PathVariable Integer roleId) {
        List<ConfPermissionDto> result = this.permissionService.getRolePermissions(roleId);
        return LcmWebResult.success(result);
    }

    /**
     * Grants permissions to the role
     */
    @PostMapping("/permissions/{roleId}")
    public LcmWebResult<Integer> rolePermissions(@PathVariable Integer roleId,
                                                 @RequestBody @Validated GrantPermissionsRequest request) {
        this.permissionService.grantPermissionsToRole(roleId, request.getPermissionIds());
        return LcmWebResult.success(0);
    }
}
