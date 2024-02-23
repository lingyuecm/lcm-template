import { configureStore } from "@reduxjs/toolkit";
import menuReducer from "./menuSlice";
import personNameReducer from "./personNameSlice";
import sidebarReducer from "./sidebarSlice";
import {TypedUseSelectorHook, useDispatch, useSelector} from "react-redux";

const store = configureStore({
    reducer: {
        personName: personNameReducer,
        sidebar: sidebarReducer,
        menu: menuReducer
    }
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;
export const useAppDispatch: () => AppDispatch = useDispatch;

export default store;
