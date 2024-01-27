import Login from '../views/login/Login'
import Dashboard from "../views/dashboard/Dashboard";
import WindowFrame from '../components/frame/WindowFrame';
import {Route} from "react-router-dom";

const routers = [
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/',
        name: 'Home',
        component: WindowFrame,
        children: [
            {
                path: '/dashboard',
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
        return (<Route path={route.path} key={index} element={<route.component/>}/>)
    }
}
