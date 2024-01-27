package com.lingyuecm.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Request for logging in, including
 * <li>{@link #phoneNo}</li>
 * <li>{@link #password}</li>
 * <li>{@link #token}</li>
 * <li>{@link #captcha}</li>
 */
@Setter
@Getter
public class LoginRequest {
    /**
     * The phone number of the user as the username
     */
    @NotNull
    private String phoneNo;
    /**
     * The password of the user
     */
    @NotNull
    private String password;
    /**
     * The token returned together with the captcha
     */
    @NotNull
    private String token;
    /**
     * The captcha
     */
    @NotNull
    private String captcha;
}
