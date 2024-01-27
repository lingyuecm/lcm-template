package com.lingyuecm.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Essential information generated on the user succeeding logging in for further use,
 * including the following fields
 * <li>{@link #token}</li>
 */
@Setter
@Getter
public class LoginDto {
    /**
     * The token generated on the user succeeding logging in for further use
     */
    private String token;
}
