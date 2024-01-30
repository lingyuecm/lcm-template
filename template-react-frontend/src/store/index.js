import { configureStore } from "@reduxjs/toolkit";
import personNameReducer from "./personNameSlice";
import sidebarReducer from "./sidebarSlice";

export default configureStore({
    reducer: {
        personName: personNameReducer,
        sidebar: sidebarReducer
    }
});
