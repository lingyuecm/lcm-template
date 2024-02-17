package com.lingyuecm.mapper;

import com.lingyuecm.dto.ConfPermissionDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper {
    List<ConfPermissionDto> selectUserPermissions(Long userId);
}
