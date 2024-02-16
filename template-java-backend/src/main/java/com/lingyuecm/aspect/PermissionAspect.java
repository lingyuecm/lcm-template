package com.lingyuecm.aspect;

import com.lingyuecm.common.Constant;
import com.lingyuecm.common.LcmWebStatus;
import com.lingyuecm.exception.LcmRuntimeException;
import com.lingyuecm.utils.ContextUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Aspect for verifying the user's permissions
 */
@Slf4j
@Aspect
@Component
public class PermissionAspect {
    @Resource
    private StringRedisTemplate redisTemplate;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) &&" +
            "!@annotation(com.lingyuecm.annotation.NoPermissionVerification)")
    public void pointcut() {
        // The handler methods that are not annotated by com.lingyuecm.annotation.NoPermissionVerification
    }
    @Before("pointcut() && @annotation(getMapping)")
    public void verifyPermission(JoinPoint joinPoint, GetMapping getMapping) {
        String controllerUrl = this.getControllerUrl(joinPoint);
        String methodUrl = getMapping.value()[0];
        if (methodUrl.isBlank()) {
            methodUrl = "/";
        }

        this.verifyPermission(ContextUtils.getUserId(), HttpMethod.GET.name(),
                this.generateUrl(controllerUrl, methodUrl));
    }

    @Before("pointcut() && @annotation(postMapping)")
    public void verifyPermission(JoinPoint joinPoint, PostMapping postMapping) {
        String controllerUrl = this.getControllerUrl(joinPoint);
        String methodUrl = postMapping.value()[0];
        if (methodUrl.isBlank()) {
            methodUrl = "/";
        }

        this.verifyPermission(ContextUtils.getUserId(), HttpMethod.POST.name(),
                this.generateUrl(controllerUrl, methodUrl));
    }

    @Before("pointcut() && @annotation(putMapping)")
    public void verifyPermission(JoinPoint joinPoint, PutMapping putMapping) {
        String controllerUrl = this.getControllerUrl(joinPoint);
        String methodUrl = putMapping.value()[0];
        if (methodUrl.isBlank()) {
            methodUrl = "/";
        }

        this.verifyPermission(ContextUtils.getUserId(), HttpMethod.PUT.name(),
                this.generateUrl(controllerUrl, methodUrl));
    }

    public String getControllerUrl(JoinPoint joinPoint) {
        String result = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class).value()[0];
        if (result.isBlank()) {
            return "/";
        }
        return result;
    }

    private String generateUrl(String controllerUrl, String methodUrl) {
        return ("/" + controllerUrl + "/" +
                /*
                Since path variables appear in method URLs only, the replacement to remove the variable names could
                be applied to these URLs only
                 */
                methodUrl.replaceAll("\\{[a-z][A-Za-z0-9_]*}", "{}"))
                .replaceAll("/{2,}", "/");
    }

    private void verifyPermission(Long userId, String httpMethod, String url) {
        log.info("Verifying permission {} {} for User {}",
                httpMethod, url, userId);
        Boolean hasPermission = this.redisTemplate.opsForSet().isMember(
                Constant.REDIS_PREFIX_USER_PERMISSION + userId,
                httpMethod + " " + url);
        if (null == hasPermission || !hasPermission) {
            log.info("No permission");
            throw new LcmRuntimeException(LcmWebStatus.UNAUTHORIZED, null);
        }
        log.info("Permission verified");
    }
}
