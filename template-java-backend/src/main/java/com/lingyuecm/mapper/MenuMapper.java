package com.lingyuecm.mapper;

import com.lingyuecm.dto.ConfMenuDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    /**
     * Selects menus granted to the user
     */
    List<ConfMenuDto> selectGrantedMenus();
}
