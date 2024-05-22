package com.lingyuecm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The details of a user, including the following fields
 * <li>{@link #userId}</li>
 * <li>{@link #loginPwd}</li>
 * <li>{@link #phoneNo}</li>
 * <li>{@link #firstName}</li>
 * <li>{@link #lastName}</li>
 * <li>{@link #grantedMenus}</li>
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BizUserDto {
    /**
     * The primary key
     */
    private Long userId;
    /**
     * The encoded password saved for the user
     */
    @JsonIgnore // Shouldn't be returned to the frontend, even though it has been encrypted
    private String loginPwd;
    private String phoneNo;
    private String firstName;
    private String lastName;
    private List<ConfMenuDto> grantedMenus;
}
