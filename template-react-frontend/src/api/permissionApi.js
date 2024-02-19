import service from "../utils/networkManager";

export function allPermissionsApi() {
    return service.request({
        method: "GET",
        url: "/permission/permissions/all"
    })
}

export function rolePermissionsApi(roleId) {
    return service.request({
        method: "GET",
        url: "/permission/permissions/" + roleId
    })
}

export function grantPermissionsApi(roleId, permissionIds) {
    return service.request({
        method: "POST",
        url: "/permission/permissions/" + roleId,
        data: { permissionIds }
    })
}

export function getPermissionsApi(params) {
    return service.request({
        method: "GET",
        url: "/permission/permissions",
        params
    })
}

export function refreshPermissionsApi() {
    return service.request({
        method: "POST",
        url: "/permission/cache"
    })
}