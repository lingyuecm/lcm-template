import { createSlice } from "@reduxjs/toolkit";

export const menuSlice = createSlice({
    name: 'menu',
    initialState: {
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
    },
    reducers: {
        setMenuTree: (state, action) => {
            state.menuTree = action.payload;
        },
        appendExpandedKey: (state, action) => {
            state.expandedKeys.push(action.payload);
        },
        removeExpandedKey: (state, action) => {
            let index = state.expandedKeys.indexOf(action.payload);
            state.expandedKeys.splice(index, 1);
        },
        setCurrentMenuPath: (state, action) => {
            state.currentMenuPath = action.payload;
        }
    }
});

export const { setMenuTree,
    appendExpandedKey,
    removeExpandedKey,
    setCurrentMenuPath
} = menuSlice.actions;

export default menuSlice.reducer;
