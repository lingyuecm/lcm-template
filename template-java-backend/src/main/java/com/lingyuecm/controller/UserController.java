package com.lingyuecm.controller;

import com.lingyuecm.common.LcmWebResult;
import com.lingyuecm.common.LcmWebStatus;
import com.lingyuecm.dto.BizUserDto;
import com.lingyuecm.dto.CaptchaDto;
import com.lingyuecm.dto.LoginDto;
import com.lingyuecm.model.BizUser;
import com.lingyuecm.request.LoginRequest;
import com.lingyuecm.request.RefreshCaptchaRequest;
import com.lingyuecm.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * Gets a new captcha
     * @see RefreshCaptchaRequest
     */
    @PostMapping(value = "/captcha")
    public LcmWebResult<CaptchaDto> captcha(@RequestBody @Validated RefreshCaptchaRequest request) {
        return LcmWebResult.success(this.userService.generateCaptcha(
                request.getCaptchaWidth(),
                request.getCaptchaHeight()));
    }

    /**
     * Logs the user in
     * @see LoginRequest
     */
    @PostMapping(value = "/login")
    public LcmWebResult<LoginDto> validateLogin(@RequestBody @Validated LoginRequest request) {
        BizUser user = new BizUser();
        user.setPhoneNo(request.getPhoneNo());
        user.setLoginPwd(request.getPassword());
        LoginDto result = this.userService.userLogin(user, request.getToken(), request.getCaptcha());

        if (null == result) {
            /*
            Failed to log in
             */
            return LcmWebResult.failure(LcmWebStatus.FAILED_TO_LOGIN);
        }
        return LcmWebResult.success(result);
    }

    @GetMapping(value = "/metadata")
    public LcmWebResult<BizUserDto> metadata() {
        BizUserDto userDto = this.userService.getMetadata();
        return LcmWebResult.success(userDto);
    }
}
