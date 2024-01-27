package com.lingyuecm.common;

import lombok.Getter;

@Getter
public enum LcmWebStatus {
    OK(0, "OK"),
    FAILED_TO_GENERATE_CAPTCHA(-1, "Failed to generate captcha"),
    INVALID_LOGIN_TOKEN(-2, "Invalid login token"),
    FAILED_TO_LOGIN(-3, "Failed to login"),
    INVALID_PARAMETER(-400, "Invalid parameter"),
    INTERNAL_SERVER_ERROR(-500, "Internal Server Error");

    private final int statusCode;
    private final String statusMessage;

    LcmWebStatus(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
