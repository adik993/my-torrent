import {checkResponse} from 'utils';
import * as types from 'actions/types';

export const torrentsIsLoading = isLoading => {
    return {
        type: types.TORRENTS_IS_LOADING,
        isLoading
    }
};

export const torrentsHasErrored = hasErrored => {
    return {
        type: types.TORRENTS_HAS_ERRORED,
        hasErrored
    }
};

export const torrentsFetchDataSiccess = torrents => {
    return {
        type: types.TORRENTS_FETCH_DATA_SUCCESS,
        torrents
    }
};

export const torrentsFetchData = (query, proxy) => dispatch => {
    dispatch(torrentsIsLoading(true));
    fetch(`/api/search?query=${query}&proxy=${proxy.id}`)
        .then(response => {
            dispatch(torrentsIsLoading(false));
            return checkResponse(response)
        })
        .then(response => response.json())
        .then(torrents => dispatch(torrentsFetchDataSiccess(torrents)))
        .catch(error => {
            console.error(error);
            dispatch(torrentsHasErrored(true))
        })
};