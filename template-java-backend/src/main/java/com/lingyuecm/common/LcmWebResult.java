package com.lingyuecm.common;

import lombok.Getter;

import static com.lingyuecm.common.LcmWebStatus.OK;

/**
 * The general data structure of the API results
 * @param <T> The data type carried
 */
@Getter
public final class LcmWebResult<T> {
    /**
     * The code representing the result
     */
    private final int resultCode;
    /**
     * The message describing the result briefly
     */
    private final String resultMessage;
    /**
     * The data carried
     */
    private T resultBody;

    private LcmWebResult() {
        this(OK);
    }

    private LcmWebResult(LcmWebStatus webStatus) {
        this.resultCode = webStatus.getStatusCode();
        this.resultMessage = webStatus.getStatusMessage();
        this.resultBody = null;
    }

    /**
     * Creates a result on success with the data to carry
     */
    public static <T> LcmWebResult<T> success(T data) {
        LcmWebResult<T> result = new LcmWebResult<>();

        result.resultBody = data;

        return result;
    }

    /**
     * Raises a failure with a status representing the failure
     */
    public static <T> LcmWebResult<T> failure(LcmWebStatus webStatus) {
        return new LcmWebResult<>(webStatus);
    }

    /**
     * Raises a failure with a status representing the failure and the details of the failure
     */
    public static <T> LcmWebResult<T> failure(LcmWebStatus webStatus, T errorBody) {
        LcmWebResult<T> result = new LcmWebResult<>(webStatus);

        result.resultBody = errorBody;

        return result;
    }
}
