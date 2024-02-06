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

export function metadataApi() {
    return service.request({
        method: 'GET',
        url: '/user/metadata'
    })
}

export function getUsersApi(params) {
    return service.request({
        method: 'GET',
        url: '/user/users',
        params: params
    })
}
