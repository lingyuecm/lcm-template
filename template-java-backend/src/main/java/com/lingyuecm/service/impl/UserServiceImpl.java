package com.lingyuecm.service.impl;

import com.lingyuecm.common.Constant;
import com.lingyuecm.common.PagedList;
import com.lingyuecm.dto.AccessTokenDto;
import com.lingyuecm.dto.BizUserDto;
import com.lingyuecm.dto.CaptchaDto;
import com.lingyuecm.dto.ConfMenuDto;
import com.lingyuecm.dto.LoginDto;
import com.lingyuecm.exception.LcmRuntimeException;
import com.lingyuecm.mapper.MenuMapper;
import com.lingyuecm.mapper.UserMapper;
import com.lingyuecm.model.BizUser;
import com.lingyuecm.service.CacheService;
import com.lingyuecm.service.JwtService;
import com.lingyuecm.service.UserService;
import com.lingyuecm.utils.ContextUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.lingyuecm.common.LcmWebStatus.FAILED_TO_GENERATE_CAPTCHA;

@Service
public class UserServiceImpl implements UserService {
    @Value("${captcha.timeout}")
    private long captchaTimeout;

    @Resource
    private JwtService jwtService;
    @Resource
    private CacheService cacheService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private StringRedisTemplate redisTemplate;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public CaptchaDto generateCaptcha(int captchaWidth, int captchaHeight) {
        CaptchaDto result = new CaptchaDto();

        String loginCaptchaId = UUID.randomUUID().toString();
        Random random = new Random();
        StringBuilder captchaBuilder = new StringBuilder(6);
        for (int m = 0; m < 6; m++) {
            captchaBuilder.append(random.nextInt(10));
        }
        String captcha = captchaBuilder.toString();

        this.redisTemplate.opsForValue().set(loginCaptchaId, captcha, this.captchaTimeout, TimeUnit.MILLISECONDS);

        result.setToken(this.jwtService.generateLoginToken(loginCaptchaId));
        try {
            result.setCaptchaImage(this.getCaptchaImageBase64(captchaWidth, captchaHeight, captcha));
        }
        catch (IOException e) {
            throw new LcmRuntimeException(FAILED_TO_GENERATE_CAPTCHA, e);
        }

        return result;
    }

    @Override
    public LoginDto userLogin(BizUser user, String loginToken, String captcha) {
        String loginCaptchaId = this.jwtService.parseLoginToken(loginToken);
        /*
        When
        1. The key has expired, which means that the captcha has expired; or
        2. The captcha provided by the user doesn't equal that saved in Redis
        the captcha will be considered invalid so that the user will fail to log in
         */
        if (Boolean.FALSE.equals(this.redisTemplate.hasKey(loginCaptchaId)) ||
                !captcha.equals(this.redisTemplate.opsForValue().get(loginCaptchaId))) {
            return null;
        }

        /*
        When
        1. No user is found by the phone number; or
        2. The password the user provided doesn't match that of the user found
        the user will fail to log in
         */
        BizUserDto userDto = this.userMapper.selectUserCredentials(user);
        if (null == userDto || !this.passwordEncoder.matches(user.getLoginPwd(), userDto.getLoginPwd())) {
            return null;
        }
        LoginDto result = new LoginDto();

        AccessTokenDto tokenDto = this.jwtService.generateAccessToken(userDto.getUserId());
        result.setToken(tokenDto.getAccessToken());
        this.cacheService.cacheUserPermissions(userDto.getUserId());

        return result;
    }

    @Override
    public BizUserDto getMetadata() {
        BizUserDto result = this.userMapper.selectMetadata();
        List<ConfMenuDto> grantedMenus = this.menuMapper.selectGrantedMenus();
        if (null == grantedMenus) {
            grantedMenus = new ArrayList<>();
        }
        result.setGrantedMenus(grantedMenus);

        return result;
    }

    @Override
    public void userLogout() {
        /*
        On logging the user out, the following things need doing
        1. Clear the cached permissions of the user
         */
        // 1
        this.redisTemplate.delete(Constant.REDIS_PREFIX_USER_PERMISSION + ContextUtils.getUserId());
    }

    @Override
    public PagedList<BizUserDto> getUsers(String criteria) {
        List<BizUserDto> result = this.userMapper.manageUsers(criteria);
        if (null == result) {
            result = new ArrayList<>();
        }
        return PagedList.paginated(this.userMapper.selectUserCount(criteria), result);
    }

    private String getCaptchaImageBase64(int captchaWidth, int captchaHeight, String captcha) throws IOException {
        BufferedImage bi = new BufferedImage(captchaWidth, captchaHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = bi.getGraphics();
        g.setColor(Color.WHITE);
        g.setFont(new Font("Times New Roman", Font.PLAIN, captchaHeight / 2));
        g.drawString(captcha, (int)(captchaWidth * 0.2), (int)(captchaHeight * 0.6));

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", bos);

        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }
}
