import * as types from 'actions/types';


export const proxies = (state = [], action) => {
    switch(action.type) {
        case types.PROXY_FETCH_DATA_SUCCESS:
            return action.proxies;
        default:
            return state;
    }
};

export const proxyIsLoading = (state = false, action) => {
    switch (action.type) {
        case types.PROXY_FETCH_DATA_IS_LOADING:
            return action.isLoading;
        default:
            return state;
    }
};

export const proxyHasErrored = (state =  false, action) => {
    switch (action.type) {
        case types.PROXY_FETCH_DATA_HAS_ERRORED:
            return action.hasErrored;
        default:
            return state;
    }
};

export const selectedProxy = (state = {}, action) => {
    switch (action.type) {
        case types.PROXY_SELECT_PROXY:
            return action.selectedProxy;
        default:
            return state;
    }
};