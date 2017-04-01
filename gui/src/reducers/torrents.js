import * as types from 'actions/types';

export const torrents = (state = [], action) => {
    switch (action.type) {
        case types.TORRENTS_FETCH_DATA_SUCCESS:
            return action.torrents;
        default:
            return state;
    }
};

export const torrentsIsLoading = (state = false, action) => {
    switch (action.type) {
        case types.TORRENTS_IS_LOADING:
            return action.isLoading;
        default:
            return state;
    }
};

export const torrentsHasErrored = (state = false, action) => {
    switch (action.type) {
        case types.TORRENTS_HAS_ERRORED:
            return action.hasErrored;
        default:
            return state;
    }
};