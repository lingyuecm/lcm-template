package com.lingyuecm.service;

import com.lingyuecm.common.PagedList;
import com.lingyuecm.dto.ConfRoleDto;

import java.util.List;

public interface RoleService {
    /**
     * Gets all available roles of the system
     * @return The list of all available roles
     * @see ConfRoleDto
     */
    List<ConfRoleDto> getAllRoles();

    /**
     * Gets all roles granted to the user
     * @param userId The user ID
     * @return The role IDs granted to the user
     * @see ConfRoleDto
     */
    List<ConfRoleDto> getUserRoles(Long userId);

    /**
     * Grants roles to the user
     * @param userId The user ID
     * @param roleIds The role IDs to grant to the user
     */
    void grantRolesToUser(Long userId, List<Integer> roleIds);

    /**
     * Gets the role list on one page
     * @param criteria The criteria for searching
     * @return The data on the page
     * @see ConfRoleDto
     */
    PagedList<ConfRoleDto> getRoles(String criteria);
}
