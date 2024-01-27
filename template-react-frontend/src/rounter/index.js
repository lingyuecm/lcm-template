import Login from '../views/login/Login'
import Dashboard from "../views/dashboard/Dashboard";
import WindowFrame from '../components/frame/WindowFrame';
import {Navigate, Route} from "react-router-dom";

const routers = [
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/*',
        name: 'Home',
        component: WindowFrame,
        children: [
            {
                path: '',
                redirect: '/dashboard',
            },
            {
                path: 'dashboard',
                name: 'Dashboard',
                component: Dashboard
            }
        ]
    }
]

export default routers

export function createRoute(route, index) {
    if (route.children && route.children.length > 0) {
        return (<Route path={route.path} key={index} element={<route.component children={route.children}/>}>
            { route.children.map((c, i) => createRoute(c, i)) }
        </Route>)
    }
    else {
        return createRedirection(route, index)
    }
}

function createRedirection(route, index) {
    if (route.redirect) {
        return <Route path={route.path} key={index} element={<Navigate to={route.redirect}/>}/>
    }
    else {
        return <Route path={route.path} key={index} element={<route.component/>}/>
    }
}
