package com.lingyuecm.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lingyuecm.common.LcmWebStatus;
import com.lingyuecm.dto.AccessTokenDto;
import com.lingyuecm.dto.AccessTokenVerificationDto;
import com.lingyuecm.exception.LcmRuntimeException;
import com.lingyuecm.service.JwtService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

import static com.lingyuecm.common.Constant.JWT_CLAIM_LOGIN_CAPTCHA_ID;
import static com.lingyuecm.common.Constant.JWT_CLAIM_USER_ID;
import static com.lingyuecm.common.LcmWebStatus.INVALID_LOGIN_TOKEN;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.access-token-timeout}")
    private long accessTokenTimeout;

    @Resource
    @Qualifier("loginTokenAlgorithm")
    private Algorithm loginTokenAlgorithm;
    @Resource
    @Qualifier("accessTokenAlgorithm")
    private Algorithm accessTokenAlgorithm;

    @Override
    public String generateLoginToken(String loginCaptchaId) {
        return JWT.create().withClaim(JWT_CLAIM_LOGIN_CAPTCHA_ID, loginCaptchaId)
                .sign(this.loginTokenAlgorithm);
    }

    @Override
    public String parseLoginToken(String token) {
        try {
            DecodedJWT jwt = JWT.require(this.loginTokenAlgorithm).build().verify(token);
            return jwt.getClaim(JWT_CLAIM_LOGIN_CAPTCHA_ID).asString();
        }
        catch (JWTVerificationException e) {
            throw new LcmRuntimeException(INVALID_LOGIN_TOKEN, e);
        }
    }

    @Override
    public AccessTokenDto generateAccessToken(Long userId) {
        AccessTokenDto result = new AccessTokenDto();

        result.setJwtId(UUID.randomUUID().toString());

        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + this.accessTokenTimeout);
        try {
            String accessToken = JWT.create().withClaim(JWT_CLAIM_USER_ID, userId)
                    .withJWTId(result.getJwtId())
                    .withNotBefore(now)
                    .withExpiresAt(expiresAt)
                    .sign(this.accessTokenAlgorithm);

            result.setAccessToken(accessToken);
            return result;
        }
        catch (JWTCreationException e) {
            /*
            This hardly happens because all claims could be converted to a valid JSON.
            The only reason this could happen is that there are problems with the signing key, since
            it is set as an environment variable

            The JWT related exceptions shouldn't be visible to the callers, actually, all JWT related things should
            be encapsulated in this service
             */
            throw new LcmRuntimeException(LcmWebStatus.FAILED_TO_GENERATE_ACCESS_TOKEN, e);
        }
    }

    @Override
    public AccessTokenVerificationDto parseAccessToken(String accessToken) {
        AccessTokenVerificationDto result = new AccessTokenVerificationDto();

        try {
            DecodedJWT jwt = JWT.require(this.accessTokenAlgorithm).build().verify(accessToken);

            result.setWebStatus(LcmWebStatus.OK);
            result.setUserId(jwt.getClaim(JWT_CLAIM_USER_ID).asLong());
            result.setJwtId(jwt.getId());
        }
        catch (JWTVerificationException e) {
            log.warn(INVALID_LOGIN_TOKEN.getStatusMessage(), e);

            result.setWebStatus(LcmWebStatus.INVALID_ACCESS_TOKEN);
        }
        catch (Exception e) {
            /*
            Some exception may occur unexpectedly. In this case, the developer may look into the log and decide whether
            to catch that exception specifically
             */
            log.warn(LcmWebStatus.INTERNAL_SERVER_ERROR.getStatusMessage(), e);

            result.setWebStatus(LcmWebStatus.INVALID_ACCESS_TOKEN);
        }
        return result;
    }
}
