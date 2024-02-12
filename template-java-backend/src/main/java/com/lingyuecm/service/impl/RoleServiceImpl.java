package com.lingyuecm.service.impl;

import com.lingyuecm.dto.ConfRoleDto;
import com.lingyuecm.mapper.RoleMapper;
import com.lingyuecm.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<ConfRoleDto> getAllRoles() {
        List<ConfRoleDto> result = this.roleMapper.selectAllRoles();
        if (null == result) {
            result = new ArrayList<>();
        }
        return result;
    }

    @Override
    public List<ConfRoleDto> getUserRoles(Long userId) {
        List<ConfRoleDto> result = this.roleMapper.selectUserRoles(userId);
        if (null == result) {
            result = new ArrayList<>();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRES_NEW)
    public void grantRolesToUser(Long userId, List<Integer> roleIds) {
        /*
        To grant roles to the user, the service should firstly delete the roles currently granted to him
        and then grant new roles to him.
        Said 2 steps should be executed in a transaction so that when the service fails to grant new roles to the user,
        the deletion will be rolled back to keep the user's current roles
         */
        this.roleMapper.deleteUserRoles(userId);
        if (!roleIds.isEmpty()) {
            this.roleMapper.insertUserRoles(userId, roleIds);
        }
    }
}
