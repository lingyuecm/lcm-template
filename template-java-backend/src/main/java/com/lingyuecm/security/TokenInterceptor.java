package com.lingyuecm.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingyuecm.common.LcmWebResult;
import com.lingyuecm.common.LcmWebStatus;
import com.lingyuecm.dto.AccessTokenVerificationDto;
import com.lingyuecm.service.JwtService;
import com.lingyuecm.utils.ContextUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import static com.lingyuecm.common.Constant.HTTP_HEADER_ACCESS_TOKEN;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private JwtService jwtService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws IOException {
        String accessToken = request.getHeader(HTTP_HEADER_ACCESS_TOKEN);
        ServletOutputStream outputStream = response.getOutputStream();
        /*
        When the user doesn't provide a token, the request will be rejected
         */
        if (null == accessToken || accessToken.isBlank()) {
            this.writeError(outputStream, LcmWebStatus.INVALID_ACCESS_TOKEN);
            return false;
        }
        AccessTokenVerificationDto verificationDto = this.jwtService.parseAccessToken(accessToken);
        /*
        When there are errors parsing the token, the request will be rejected
         */
        if (LcmWebStatus.OK != verificationDto.getWebStatus()) {
            this.writeError(outputStream, verificationDto.getWebStatus());
            return false;
        }
        /*
        The user ID should be cached on succeeding parsing the token
         */
        ContextUtils.setUserId(verificationDto.getUserId());
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                           @NonNull Object handler, @Nullable ModelAndView modelAndView) {
        /*
        The cached variables should be cleared on finishing processing the request
         */
        ContextUtils.clearContext();
    }

    private void writeError(ServletOutputStream outputStream, LcmWebStatus webStatus) throws IOException {
        LcmWebResult<Boolean> result = LcmWebResult.failure(webStatus);

        outputStream.print(this.objectMapper.writeValueAsString(result));
    }
}
