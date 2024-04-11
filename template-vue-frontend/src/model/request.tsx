export type RefreshCaptchaRequest = {
    captchaWidth: number,
    captchaHeight: number
}

export type LoginRequest = {
    phoneNo: string,
    password: string,
    captcha: string,
    token: string
}

type PageData = {
    pageNo: number,
    pageSize: number
}

export type GetUsersRequest = PageData & {
    criteria?: string
}

export type GetRolesRequest = PageData & {
    criteria?: string
}

export type GetPermissionsRequest = PageData & {
    httpMethod?: string,
    permissionUrl?: string
}