package com.lingyuecm.service;

public interface CacheService {
    /**
     * Caches the user's permissions to improve the performance of permission verification
     * @param userId The user ID
     */
    void cacheUserPermissions(Long userId);
}
