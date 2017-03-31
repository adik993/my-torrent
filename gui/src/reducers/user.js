import * as types from "actions/types";

export const userIsLoading = (state = true, action) => {
    switch (action.type) {
        case types.USER_IS_LOADING:
            return action.isLoading;
        default:
            return state;
    }
};

export const userHasErrored = (state = false, action) => {
    switch (action.type) {
        case types.USER_HAS_ERRORED:
            return action.hasErrored;
        default:
            return state;
    }
};

export const user = (state = {}, action) => {
    switch (action.type) {
        case types.USER_FETCH_DATA_SUCCESS:
            return action.user;
        default:
            return state;
    }
};