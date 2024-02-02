package com.lingyuecm.aspect;

import com.lingyuecm.common.LcmWebResult;
import com.lingyuecm.exception.ConventionViolationException;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Some conventions of the project
 * <ol type="i">
 *     <li>The handler methods should be annotated by {@link GetMapping}, {@link PutMapping} or {@link PostMapping}</li>
 *     <li>The return type of handler methods should be {@link LcmWebResult}</li>
 * </ol>
 * When convention violation is detected, a {@link ConventionViolationException} will be thrown
 */
@Aspect
@Component
@RestController
public class ConventionAspect {
    @Before("@within(org.springframework.web.bind.annotation.RestController) &&" +
            "!@annotation(org.springframework.web.bind.annotation.GetMapping) &&" +
            "!@annotation(org.springframework.web.bind.annotation.PutMapping) &&" +
            "!@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void checkRequestMappingAnnotation() {
        throw new ConventionViolationException("Invalid annotation on handler methods");
    }

    @AfterReturning(value = "@within(org.springframework.web.bind.annotation.RestController) &&" +
            "(@annotation(org.springframework.web.bind.annotation.GetMapping) ||" +
            "        @annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
            "        @annotation(org.springframework.web.bind.annotation.PostMapping))",
    returning = "result")
    public void afterApiReturning(Object result) {
        if (!(result instanceof LcmWebResult<?>)) {
            throw new ConventionViolationException("Invalid return type");
        }
    }
}
