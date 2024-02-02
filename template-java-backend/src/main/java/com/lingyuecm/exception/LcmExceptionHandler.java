package com.lingyuecm.exception;

import com.lingyuecm.aspect.ConventionAspect;
import com.lingyuecm.common.LcmWebResult;
import com.lingyuecm.common.LcmWebStatus;
import com.lingyuecm.utils.ContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class LcmExceptionHandler {
    /**
     * Handles the convention violation errors
     */
    @ExceptionHandler({ConventionViolationException.class})
    public LcmWebResult<Boolean> handleConventionViolationException(ConventionViolationException e) {
        log.warn("Convention violation detected: {}. refer to {} for help",
                e.getMessage(),
                ConventionAspect.class.getName());
        return LcmWebResult.failure(LcmWebStatus.CONVENTION_VIOLATED);
    }
    /**
     * Handles the customized runtime exception
     */
    @ExceptionHandler({LcmRuntimeException.class})
    public LcmWebResult<Boolean> handleLcmRuntimeException(LcmRuntimeException e) {
        log.warn("Internal Server Error: ", e.getCause());
        ContextUtils.clearContext();
        return LcmWebResult.failure(e.getWebStatus());
    }

    /**
     * Handles the validation errors
     * @return The list of errors in the form <b style="font-size: 11px;"><i>field: error text</i></b>
     */
    @ExceptionHandler({BindException.class})
    public LcmWebResult<List<String>> handleBindException(BindException e) {
        log.warn("Invalid Parameter:", e);
        ContextUtils.clearContext();
        List<String> result = e.getAllErrors().stream().map(error -> error.getObjectName() + ": " + error.getDefaultMessage()).toList();
        return LcmWebResult.failure(LcmWebStatus.INVALID_PARAMETER, result);
    }

    /**
     * Handles unexpected exceptions
     */
    @ExceptionHandler({Exception.class})
    public LcmWebResult<Boolean> handleException(Exception e) {
        log.warn("Error: ", e);
        ContextUtils.clearContext();
        return LcmWebResult.failure(LcmWebStatus.INTERNAL_SERVER_ERROR);
    }
}
