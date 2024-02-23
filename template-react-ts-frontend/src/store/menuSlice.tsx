import {createSlice, Slice, PayloadAction} from "@reduxjs/toolkit";

export type Menu = {path: string, title: string, children?: Menu[]};
type MenuState = {
    menuTree: Menu[],
    expandedKeys: string[],
    currentMenuPath: string
}

const initialState: MenuState = {
    menuTree: [
        {
            path: '/',
            title: 'Home'
        },
        {
            path: '/backendMgmt',
            title: 'Backend Management',
            children: [
                {
                    path: '/backendMgmt/userMgmt',
                    title: 'User Management'
                },
                {
                    path: '/backendMgmt/roleMgmt',
                    title: 'Role Management'
                },
                {
                    path: '/backendMgmt/permissionMgmt',
                    title: 'Permission Management'
                }
            ]
        }
    ],
    expandedKeys: [],
    currentMenuPath: ''
}

export const menuSlice: Slice = createSlice({
    name: 'menu',
    initialState,
    reducers: {
        setMenuTree: (state: MenuState, action: PayloadAction<Menu[]>):void => {
            state.menuTree = action.payload;
        },
        appendExpandedKey: (state: MenuState, action: PayloadAction<string>):void => {
            state.expandedKeys.push(action.payload);
        },
        removeExpandedKey: (state: MenuState, action: PayloadAction<string>):void => {
            let index = state.expandedKeys.indexOf(action.payload);
            state.expandedKeys.splice(index, 1);
        },
        setCurrentMenuPath: (state: MenuState, action: PayloadAction<string>):void => {
            state.currentMenuPath = action.payload;
        }
    }
});

export const {
    setMenuTree,
    appendExpandedKey,
    removeExpandedKey,
    setCurrentMenuPath
} = menuSlice.actions;

export default menuSlice.reducer;
