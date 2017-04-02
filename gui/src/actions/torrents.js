import {checkResponse} from "utils";
import * as types from "actions/types";

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

export const torrentsFetchData = (query, provider) => dispatch => {
    dispatch(torrentsIsLoading(true));
    dispatch(torrentsHasErrored(false));
    fetch(`/api/search?query=${query}&provider=${provider}`)
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

export const torrentsSelectTorrentSuccess = (torrent, chosen) => {
    return {
        type: types.TORRENTS_SELECT_TORRENT_SUCCESS,
        torrent,
        chosen
    }
};

export const torrentsSelectTorrent = (torrent, selected) => dispatch => {
    fetch(`/api/result/select?id=${torrent.id}&selected=${selected}`, {
        method: 'POST'
    })
        .then(response => checkResponse(response))
        .then(response => dispatch(torrentsSelectTorrentSuccess(torrent, selected)))
        .catch(error => {
            console.error(error);
        });
};