import {createSlice, PayloadAction} from "@reduxjs/toolkit";

type PersonNameState = {
    firstName: string,
    lastName: string
}

const initialState: PersonNameState = {
    firstName: '',
    lastName: ''
}

export const personNameSlice = createSlice({
    name: 'personName',
    initialState,
    reducers: {
        setPersonName: (state: PersonNameState, action: PayloadAction<PersonNameState>) => {
            state.firstName = action.payload.firstName;
            state.lastName = action.payload.lastName;
        }
    }
});

export const { setPersonName } = personNameSlice.actions;

export default personNameSlice.reducer;