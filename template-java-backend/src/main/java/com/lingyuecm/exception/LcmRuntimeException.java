package com.lingyuecm.exception;

import com.lingyuecm.common.LcmWebStatus;
import lombok.Getter;

/**
 * The customized runtime exception.
 * <br/>The developer should throw this exception with whenever he can catch a third-party (or JDK specified) exception.
 */
@Getter
public final class LcmRuntimeException extends RuntimeException {
    /**
     * The status the developer wants to carry by this exception
     */
    private final LcmWebStatus webStatus;

    public LcmRuntimeException(LcmWebStatus webStatus, Throwable t) {
        super(t);
        this.webStatus = webStatus;
    }
}
