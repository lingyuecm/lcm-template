import service from "../utils/networkManager"

export function getAllRolesApi() {
    return service.request({
        method: "GET",
        url: "/role/roles/all"
    })
}

export function getUserRolesApi(userId) {
    return service.request({
        method: "GET",
        url: "/role/roles/" + userId
    })
}

export function grantRolesApi(userId, roleIds) {
    return service.request({
        method: "POST",
        url: "/role/roles/" + userId,
        data: { roleIds }
    })
}

export function getRolesApi(params) {
    return service.request({
        method: "GET",
        url: "/role/roles",
        params
    })
}