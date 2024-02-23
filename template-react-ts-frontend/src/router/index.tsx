import React from "react";
import Login from "../views/login/Login";
import {Navigate, Route} from "react-router-dom";
import WindowFrame from "../components/frame/WindowFrame";
import Dashboard from "../views/dashboard/Dashboard";
import PermissionManagement from "../views/management/PermissionManagement";
import UserManagement from "../views/management/UserManagement";
import RoleManagement from "../views/management/RoleManagement";

export type RouterModel = { path: string, component: React.FC<{children: RouterModel[]}>, children?: RouterModel[] };

const routers: RouterModel[] = [
    {
        path: "/login",
        component: Login
    },
    {
        path: "/*",
        component: WindowFrame,
        children: [
            {
                path: "dashboard",
                component: Dashboard
            }
        ]
    },
    {
        path: "/backendMgmt/*",
        component: WindowFrame,
        children: [
            {
                path: "userMgmt",
                component: UserManagement
            },
            {
                path: "roleMgmt",
                component: RoleManagement
            },
            {
                path: "permissionMgmt",
                component: PermissionManagement
            }
        ]
    }
];

export default routers;

export function createRoute(route: RouterModel, index: number, accessToken: string): React.JSX.Element {
    if (route.children && route.children.length > 0) {
        const C: React.ReactNode = verifyComponent({
            component: <route.component children={route.children}/>,
            path: route.path,
            accessToken
        });
        return (<Route path={route.path} key={index}
                       element={C}>
        {route.children.map((c: RouterModel, i: number) => createRoute(c, i, accessToken)) }
        </Route>)
    }
    else {
        const C: React.ReactNode = verifyComponent({component: <route.component children={[]}/>, path: route.path, accessToken});
        return <Route path={route.path} key={index} element={C}/>
    }
}

type ComponentParams = {component: React.ReactNode, path: string, accessToken: string};
function verifyComponent(params: ComponentParams): React.ReactNode {
    if (params.accessToken || params.path === "/login") {
        return params.component;
    }
    else {
        return <Navigate to={"/login"}/>
    }
}
