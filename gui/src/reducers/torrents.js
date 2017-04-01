import * as types from "actions/types";
import _ from "lodash";

export const torrents = (state = [], action) => {
    switch (action.type) {
        case types.TORRENTS_FETCH_DATA_SUCCESS:
            return action.torrents;
        case types.TORRENTS_SELECT_TORRENT_SUCCESS:
            let index = _.findIndex(state, {id: action.torrent.id});
            let tmp = state.slice();
            tmp[index] = {...action.torrent, chosen: action.chosen};
            return tmp;
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