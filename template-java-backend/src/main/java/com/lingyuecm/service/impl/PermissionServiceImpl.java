package com.lingyuecm.service.impl;

import com.lingyuecm.common.Constant;
import com.lingyuecm.common.PagedList;
import com.lingyuecm.dto.ConfPermissionDto;
import com.lingyuecm.mapper.PermissionMapper;
import com.lingyuecm.model.ConfPermission;
import com.lingyuecm.service.CacheService;
import com.lingyuecm.service.PermissionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {
    @Resource
    private CacheService cacheService;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private StringRedisTemplate redisTemplate;

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

    @Override
    public void refreshPermissionCache() {
        Set<String> userPermissionKeys = this.redisTemplate.keys(Constant.REDIS_PREFIX_USER_PERMISSION + "*");
        if (null == userPermissionKeys) {
            log.info("No permissions cached");
            return;
        }
        for (String userPermissionKey : userPermissionKeys) {
            Long userId = Long.valueOf(userPermissionKey.split("_")[1]);
            log.info("Caching permissions for user {}", userId);
            this.cacheService.cacheUserPermissions(userId);
            log.info("Permissions cached for user {}", userId);
        }
    }
}
