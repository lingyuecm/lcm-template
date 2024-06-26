import service from "../utils/networkManager"
import {
    type LcmWebResult,
    type PagedList
} from '@/model/model';
import {type ConfRoleDto} from '@/model/dto';
import {type GetRolesRequest} from '@/model/request';

export function getAllRolesApi(): Promise<LcmWebResult<ConfRoleDto[]>> {
    return service.request({
        method: "GET",
        url: "/role/roles/all"
    })
}

export function getUserRolesApi(userId: number): Promise<LcmWebResult<ConfRoleDto[]>> {
    return service.request({
        method: "GET",
        url: "/role/roles/" + userId
    })
}

export function grantRolesApi(userId: number, roleIds: number[]): Promise<LcmWebResult<number>> {
    return service.request({
        method: "POST",
        url: "/role/roles/" + userId,
        data: { roleIds }
    })
}

export function getRolesApi(params: GetRolesRequest): Promise<LcmWebResult<PagedList<ConfRoleDto>>> {
    return service.request({
        method: "GET",
        url: "/role/roles",
        params
    })
}