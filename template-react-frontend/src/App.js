import './App.css';
import { Routes } from "react-router-dom";
import routers from './rounter';
import { createRoute } from "./rounter";
import redirections from "./rounter/redirection";
import {createRedirection} from "./rounter/redirection";
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';
import {getAccessToken} from "./utils/cacheManager";

function App() {
  const accessToken = getAccessToken();
  return (
    <Routes>
        { routers.map((r, index) => createRoute(r, index, accessToken))}
      { redirections.map((r, index) => createRedirection(r, index)) }
    </Routes>
  )
}

export default App;
