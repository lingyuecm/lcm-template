package com.lingyuecm.mapper;

import com.lingyuecm.dto.BizUserDto;
import com.lingyuecm.model.BizUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * Selects the user's credentials
     * @param user The query condition, see {@link BizUser#getPhoneNo()}
     * @return The details of the user including the user's credentials, see {@link BizUserDto#getLoginPwd()}
     */
    BizUserDto selectUserCredentials(BizUser user);

    /**
     * Selects the metadata to display on the web page
     * @return The metadata desired
     */
    BizUserDto selectMetadata();

    /**
     * Selects the user list for the admin
     */
    List<BizUserDto> manageUsers(String criteria);

    /**
     * Selects the total count of users by certain criteria
     */
    long selectUserCount(String criteria);
}
