package com.lingyuecm.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The details of an access token, including
 * <li>{@link #accessToken}</li>
 * <li>{@link #jwtId}</li>
 */
@Setter
@Getter
public class AccessTokenDto {
    /**
     * The token itself
     */
    private String accessToken;
    /**
     * The JWT ID of the token
     */
    private String jwtId;
}
