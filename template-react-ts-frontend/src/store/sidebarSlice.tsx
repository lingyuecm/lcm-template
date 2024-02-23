import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import {RootState} from "./index";

export type SidebarState = {
    sidebarExpanded: boolean
}

const initialState: SidebarState = {
    sidebarExpanded: false
}

export const sidebarSlice = createSlice({
    name: "sidebar",
    initialState,
    reducers: {
        setSidebarExpanded: (state: SidebarState, action: PayloadAction<boolean>) => {
            state.sidebarExpanded = action.payload;
        }
    }
});

export const { setSidebarExpanded } = sidebarSlice.actions;
export const selectSidebarExpanded = (state: RootState) => state.sidebar.sidebarExpanded;

export default sidebarSlice.reducer;