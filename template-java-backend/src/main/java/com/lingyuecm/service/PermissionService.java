package com.lingyuecm.service;

import com.lingyuecm.dto.ConfPermissionDto;

import java.util.List;

public interface PermissionService {
    /**
     * Gets all available permissions of the system
     * @see ConfPermissionDto
     */
    List<ConfPermissionDto> getAllPermissions();

    /**
     * Gets all permissions granted to the role
     * @param roleId The role ID
     * @see ConfPermissionDto
     */
    List<ConfPermissionDto> getRolePermissions(Integer roleId);

    /**
     * Grants permissions to the role
     * @param roleId The role ID
     * @param permissionIds The permission IDs to grant to the role
     */
    void grantPermissionsToRole(Integer roleId, List<Integer> permissionIds);
}
