import {Navigate, Route} from "react-router-dom";

const redirections = [
    {
        path: '/',
        redirect: '/dashboard'
    }
]

export default redirections;

export function createRedirection(redirection, index) {
    return <Route path={redirection.path} key={index} element={<Navigate to={redirection.redirect}/>}/>
}
