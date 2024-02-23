import {Navigate, Route} from "react-router-dom";

type RedirectionModel = {
    path: string,
    redirect: string
}
const redirections: RedirectionModel[] = [
    {
        path: '/',
        redirect: '/dashboard'
    }
]

export default redirections;

export function createRedirection(redirection: RedirectionModel, index: number) {
    return <Route path={redirection.path} key={index} element={<Navigate to={redirection.redirect} state={{
        from: redirection.path
    }}/>}/>
}
