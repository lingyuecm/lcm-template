import { createSlice } from "@reduxjs/toolkit";

export const sidebarSlice = createSlice({
    name: "sidebar",
    initialState: {
        sidebarExpanded: false
    },
    reducers: {
        setSidebarExpanded: (state, action) => {
            state.sidebarExpanded = action.payload;
        }
    }
});

export const { setSidebarExpanded } = sidebarSlice.actions;

export default sidebarSlice.reducer;