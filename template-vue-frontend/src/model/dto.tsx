import type { Menu } from '@/model/model'

export type CaptchaDto = {
    token: string,
    captchaImage: string
}

export type LoginDto = {
    token: string
}

export type BizUserDto = {
    userId: number,
    phoneNo: string,
    firstName: string,
    lastName: string,
    grantedMenus: Menu[]
}

export type ConfRoleDto = {
    roleId: number,
    roleName: string
}

export type ConfPermissionDto = {
    permissionId: number,
    httpMethod: string,
    permissionUrl: string
}
