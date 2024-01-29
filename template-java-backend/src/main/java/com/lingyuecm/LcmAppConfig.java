package com.lingyuecm;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingyuecm.security.TokenInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;

@Configuration
@MapperScan(basePackages = {"com.lingyuecm.mapper"})
public class LcmAppConfig implements WebMvcConfigurer, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Value("${jwt.secrets.login-token}")
    private String loginTokenSecret;
    @Value("${jwt.secrets.access-token}")
    private String accessTokenSecret;
    @Value("${bcrypt.strength}")
    private int bcryptStrength;
    @Value("${bcrypt.secret}")
    private String bcryptSecret;
    @Value("${interceptors.token.exclusion-patterns}")
    private List<String> tokenExclusions;

    @Bean
    public Algorithm loginTokenAlgorithm() {
        return Algorithm.HMAC256(this.loginTokenSecret);
    }

    @Bean
    public Algorithm accessTokenAlgorithm() {
        return Algorithm.HMAC512(this.accessTokenSecret);
    }

    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();

        redisTemplate.setConnectionFactory(connectionFactory);

        return redisTemplate;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(this.bcryptStrength,
                new SecureRandom(this.bcryptSecret.getBytes(StandardCharsets.UTF_8)));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.applicationContext.getBean(TokenInterceptor.class))
                .addPathPatterns("/**")
                .excludePathPatterns(this.tokenExclusions);
    }
}
