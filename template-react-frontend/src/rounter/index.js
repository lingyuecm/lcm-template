import Login from "../views/login/Login";
import Dashboard from "../views/dashboard/Dashboard";
import WindowFrame from "../components/frame/WindowFrame";
import {Navigate, Route} from "react-router-dom";
import UserManagement from "../views/management/UserManagement";
import RoleManagement from "../views/management/RoleManagement";
import PermissionManagement from "../views/management/PermissionManagement";

const routers = [
    {
        path: "/login",
        name: "Login",
        component: Login
    },
    {
        path: "/*",
        name: "Home",
        component: WindowFrame,
        children: [
            {
                path: "dashboard",
                name: "Dashboard",
                component: Dashboard
            }
        ]
    },
    {
        path: "/backendMgmt/*",
        name: "Backend Management",
        component: WindowFrame,
        children: [
            {
                path: "userMgmt",
                name: "User Management",
                component: UserManagement
            },
            {
                path: "roleMgmt",
                name: "Role Management",
                component: RoleManagement
            },
            {
                path: "permissionMgmt",
                name: "Permission management",
                component: PermissionManagement
            }
        ]
    }
]

export default routers

export function createRoute(route, index, accessToken) {
    if (route.children && route.children.length > 0) {
        return (<Route path={route.path} key={index}
                       element={verifyComponent(<route.component children={route.children}/>, route.path, accessToken)}>
            {route.children.map((c, i) => createRoute(c, i, accessToken)) }
        </Route>)
    }
    else {
        return <Route path={route.path} key={index} element={verifyComponent(<route.component/>, route.path, accessToken)}/>
    }
}

function verifyComponent(component, path, accessToken) {
    if (accessToken || path === "/login") {
        return component;
    }
    else {
        return <Navigate to={"/login"}/>
    }
}
