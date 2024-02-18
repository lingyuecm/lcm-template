package com.lingyuecm.mapper;

import com.lingyuecm.dto.ConfPermissionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionMapper {
    /**
     * Selects permissions granted to the user
     * @param userId The user ID
     */
    List<ConfPermissionDto> selectUserPermissions(Long userId);

    /**
     * Selects all available permissions of the system
     */
    List<ConfPermissionDto> selectAllPermissions();

    /**
     * Selects all permissions granted to the role
     * @param roleId The role ID
     */
    List<ConfPermissionDto> selectRolePermissions(Integer roleId);

    /**
     * Deletes all permissions granted to the role
     * @param roleId The role ID
     * @see #insertRolePermissions(Integer, List)
     */
    void deleteRolePermissions(Integer roleId);

    /**
     * Grants permissions to the role
     * @param roleId The role ID
     * @param permissionIds The permission IDs to grant to the role
     */
    void insertRolePermissions(@Param("roleId") Integer roleId,
                               @Param("permissionIds") List<Integer> permissionIds);
}
