package com.lingyuecm.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The details of a user, including the following fields
 * <li>{@link #loginPwd}</li>
 */
@Setter
@Getter
public class BizUserDto {
    /**
     * The encoded password saved for the user
     */
    private String loginPwd;
}
