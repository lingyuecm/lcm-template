import React from 'react';
import routers, {createRoute} from './router';
import {Routes} from "react-router-dom";
import redirections, {createRedirection} from "./router/redirection";

function App(): React.ReactElement {
  return <Routes>
    { routers.map((r, index) => createRoute(r, index, "AAA")) }
    { redirections.map((r, index) => createRedirection(r, index)) }
  </Routes>
}

export default App;
