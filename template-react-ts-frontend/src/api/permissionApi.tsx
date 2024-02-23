import service from "../utils/networkManager";
import {LcmWebResult, PagedList} from "../model/model";
import {GetPermissionsRequest} from "../model/request";
import {ConfPermissionDto} from "../model/dto";

export function allPermissionsApi(): Promise<LcmWebResult<ConfPermissionDto[]>> {
    return service.request({
        method: "GET",
        url: "/permission/permissions/all"
    })
}

export function rolePermissionsApi(roleId: number): Promise<LcmWebResult<ConfPermissionDto[]>> {
    return service.request({
        method: "GET",
        url: "/permission/permissions/" + roleId
    })
}

export function grantPermissionsApi(roleId: number, permissionIds: number[]): Promise<LcmWebResult<number>> {
    return service.request({
        method: "POST",
        url: "/permission/permissions/" + roleId,
        data: { permissionIds }
    })
}

export function getPermissionsApi(params: GetPermissionsRequest): Promise<LcmWebResult<PagedList<ConfPermissionDto>>> {
    return service.request({
        method: "GET",
        url: "/permission/permissions",
        params
    })
}

export function refreshPermissionsApi(): Promise<LcmWebResult<number>> {
    return service.request({
        method: "POST",
        url: "/permission/cache"
    })
}