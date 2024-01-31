package com.lingyuecm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * The details of a user, including the following fields
 * <li>{@link #userId}</li>
 * <li>{@link #loginPwd}</li>
 * <li>@{@link #firstName}</li>
 * <li>@{@link #lastName}</li>
 */
@Setter
@Getter
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
    private String firstName;
    private String lastName;
}
