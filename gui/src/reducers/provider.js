import * as types from "actions/types";


export const providers = (state = [], action) => {
    switch (action.type) {
        case types.PROVIDER_FETCH_DATA_SUCCESS:
            return action.providers;
        default:
            return state;
    }
};

export const providerIsLoading = (state = false, action) => {
    switch (action.type) {
        case types.PROVIDER_FETCH_DATA_IS_LOADING:
            return action.isLoading;
        default:
            return state;
    }
};

export const providerHasErrored = (state = false, action) => {
    switch (action.type) {
        case types.PROVIDER_FETCH_DATA_HAS_ERRORED:
            return action.hasErrored;
        default:
            return state;
    }
};

export const selectedProvider = (state = {}, action) => {
    switch (action.type) {
        case types.PROVIDER_SELECT_PROVIDER:
            return action.selectedProvider;
        default:
            return state;
    }
};