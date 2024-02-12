package com.lingyuecm.mapper;

import com.lingyuecm.dto.ConfRoleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
    /**
     * Selects all available roles of the system
     */
    List<ConfRoleDto> selectAllRoles();

    /**
     * Selects all roles granted to the user
     */
    List<ConfRoleDto> selectUserRoles(Long userId);

    /**
     * Deletes all roles granted to the user
     * @param userId The user ID
     * @see #insertUserRoles(Long, List)
     */
    void deleteUserRoles(Long userId);

    /**
     * Grants roles to the user
     * @param userId The user ID
     * @param roleIds The role IDs
     * @see #deleteUserRoles(Long)
     */
    void insertUserRoles(@Param("userId") Long userId,
                         @Param("roleIds") List<Integer> roleIds);
}
