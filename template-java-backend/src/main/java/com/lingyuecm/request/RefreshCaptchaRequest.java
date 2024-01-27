package com.lingyuecm.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Request for refreshing the captcha, including
 * <li>{@link #captchaWidth}</li>
 * <li>{@link #captchaHeight}</li>
 */
@Setter
@Getter
public class RefreshCaptchaRequest {
    /**
     * The desired width of the captcha image
     */
    @Min(30)
    @Max(300)
    @NotNull
    private Integer captchaWidth;
    /**
     * The desired height of the captcha image
     */
    @Min(30)
    @Max(300)
    @NotNull
    private Integer captchaHeight;
}
