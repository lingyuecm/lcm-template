package com.lingyuecm.service.impl;

import com.lingyuecm.common.PagedList;
import com.lingyuecm.dto.ConfPermissionDto;
import com.lingyuecm.mapper.PermissionMapper;
import com.lingyuecm.model.ConfPermission;
import com.lingyuecm.service.PermissionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<ConfPermissionDto> getAllPermissions() {
        List<ConfPermissionDto> result = this.permissionMapper.selectAllPermissions();
        if (null == result) {
            result = new ArrayList<>();
        }
        return result;
    }

    @Override
    public List<ConfPermissionDto> getRolePermissions(Integer roleId) {
        List<ConfPermissionDto> result = this.permissionMapper.selectRolePermissions(roleId);
        if (null == result) {
            result = new ArrayList<>();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRES_NEW)
    public void grantPermissionsToRole(Integer roleId, List<Integer> permissionIds) {
        /*
        To grant permissions to the role, the service should firstly delete the permissions currently granted to him
        and then grant new permissions to him.
        Said 2 steps should be executed in a transaction so that when the service fails to grant new permissions to
        the role, the deletion will be rolled back to keep the role's current permissions
         */
        this.permissionMapper.deleteRolePermissions(roleId);
        if (!permissionIds.isEmpty()) {
            this.permissionMapper.insertRolePermissions(roleId, permissionIds);
        }
    }

    @Override
    public PagedList<ConfPermissionDto> getPermissions(ConfPermission permission) {
        List<ConfPermissionDto> result = this.permissionMapper.managePermissions(permission);
        if (null == result) {
            result = new ArrayList<>();
        }
        return PagedList.paginated(this.permissionMapper.selectPermissionCount(permission), result);
    }
}
