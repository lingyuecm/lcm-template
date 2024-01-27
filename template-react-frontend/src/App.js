import './App.css';
import { Routes } from "react-router-dom";
import routers from './rounter';
import { createRoute } from "./rounter";
import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';

function App() {
  return (
    <Routes>
        { routers.map((r, index) => createRoute(r, index))}
    </Routes>
  )
}

export default App;
