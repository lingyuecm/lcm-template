package com.lingyuecm.service;

import com.lingyuecm.common.PagedList;
import com.lingyuecm.dto.ConfPermissionDto;
import com.lingyuecm.model.ConfPermission;

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

    /**
     * Gets the permission list for the admin
     * @param permission Conditions for searching,
     * see {@link ConfPermission#getHttpMethod()}, {@link ConfPermission#getPermissionUrl()}
     * @see ConfPermissionDto
     */
    PagedList<ConfPermissionDto> getPermissions(ConfPermission permission);

    /**
     * Refreshes the cached user permissions
     */
    void refreshPermissionCache();
}
