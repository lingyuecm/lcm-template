package com.lingyuecm.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Essential information generated for the captcha, including the following fields
 * <li>{@link #token}</li>
 * <li>{@link #captchaImage}</li>
 */
@Setter
@Getter
public class CaptchaDto {
    /**
     * The token used for logging in
     */
    private String token;
    /**
     * The base64 text of the captcha image in png format
     */
    private String captchaImage;
}
