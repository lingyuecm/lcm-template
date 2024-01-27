import service from '../utils/networkManager'

export function refreshCaptchaApi(requestBody) {
    return service.request({
        method: 'POST',
        url: '/user/captcha',
        data: requestBody
    })
}

export function loginApi(requestBody) {
    return service.request({
        method: 'POST',
        url: '/user/login',
        data: requestBody
    })
}
