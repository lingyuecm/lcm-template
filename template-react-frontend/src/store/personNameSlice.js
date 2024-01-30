import { createSlice } from "@reduxjs/toolkit";

export const personNameSlice = createSlice({
    name: 'personName',
    initialState: {
        firstName: '',
        lastName: ''
    },
    reducers: {
        setPersonName: (state, action) => {
            state.firstName = action.payload.firstName;
            state.lastName = action.payload.lastName;
        }
    }
});

export const { setPersonName } = personNameSlice.actions;

export default personNameSlice.reducer;