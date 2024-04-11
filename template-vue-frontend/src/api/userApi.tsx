import service from "../utils/networkManager"
import {
    type LcmWebResult,
    type PagedList
} from '@/model/model';
import {
    type BizUserDto,
    type CaptchaDto,
    type LoginDto
} from '@/model/dto';
import {
    type GetUsersRequest,
    type LoginRequest,
    type RefreshCaptchaRequest
} from '@/model/request';

export function refreshCaptchaApi(requestBody: RefreshCaptchaRequest): Promise<LcmWebResult<CaptchaDto>> {
    return service.request({
        method: "POST",
        url: "/user/captcha",
        data: requestBody
    })
}

export function loginApi(requestBody: LoginRequest): Promise<LcmWebResult<LoginDto>> {
    return service.request({
        method: "POST",
        url: "/user/login",
        data: requestBody
    })
}

export function metadataApi(): Promise<LcmWebResult<BizUserDto>> {
    return service.request({
        method: "GET",
        url: "/user/metadata"
    })
}

export function logoutApi(): Promise<LcmWebResult<number>> {
    return service.request({
        method: "POST",
        url: "/user/logout"
    })
}

export function getUsersApi(params: GetUsersRequest): Promise<LcmWebResult<PagedList<BizUserDto>>> {
    return service.request({
        method: "GET",
        url: "/user/users",
        params: params
    })
}
