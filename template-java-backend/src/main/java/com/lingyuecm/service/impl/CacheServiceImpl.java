package com.lingyuecm.service.impl;

import com.lingyuecm.common.Constant;
import com.lingyuecm.dto.ConfPermissionDto;
import com.lingyuecm.mapper.PermissionMapper;
import com.lingyuecm.service.CacheService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CacheServiceImpl implements CacheService {
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private StringRedisTemplate redisTemplate;

    @Override
    public void cacheUserPermissions(Long userId) {
        List<ConfPermissionDto> userPermissions = this.permissionMapper.selectUserPermissions(userId);
        if (null == userPermissions) {
            userPermissions = new ArrayList<>();
        }
        Set<String> permissionSet = userPermissions.stream()
                .map(p -> p.getHttpMethod() + " " + p.getPermissionUrl())
                .collect(Collectors.toSet());
        if (permissionSet.isEmpty()) {
            /*
            Redis couldn't set empty sets at a key, so when the user doesn't have any permissions,
            a placeholder will be added to the "empty set"
             */
            permissionSet.add("* /**");
        }
        String userPermissionKey = Constant.REDIS_PREFIX_USER_PERMISSION + userId;
        this.redisTemplate.delete(userPermissionKey);
        this.redisTemplate.opsForSet().add(userPermissionKey, permissionSet.toArray(String[]::new));
    }
}
