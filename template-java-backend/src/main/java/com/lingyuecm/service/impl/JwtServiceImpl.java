package com.lingyuecm.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lingyuecm.exception.LcmRuntimeException;
import com.lingyuecm.service.JwtService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.lingyuecm.common.Constant.JWT_CLAIM_LOGIN_CAPTCHA_ID;
import static com.lingyuecm.common.LcmWebStatus.INVALID_LOGIN_TOKEN;

@Service
public class JwtServiceImpl implements JwtService {
    @Resource
    @Qualifier("loginTokenAlgorithm")
    private Algorithm loginTokenAlgorithm;

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
}
