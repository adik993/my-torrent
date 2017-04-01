import {checkResponse} from 'utils';
import * as types from 'actions/types';

export const proxyFetchDataIsLoading = isLoading => {
    return {
        type: types.PROXY_FETCH_DATA_IS_LOADING,
        isLoading
    }
};

export const proxyFetchDataHasErrored = hasErrored => {
    return {
        type: types.PROXY_FETCH_DATA_HAS_ERRORED,
        hasErrored
    };
};

export const proxyFetchDataSuccess = proxies => {
    return {
        type: types.PROXY_FETCH_DATA_SUCCESS,
        proxies
    }
};

export const proxySelectProxy = selectedProxy => {
    return {
        type: types.PROXY_SELECT_PROXY,
        selectedProxy
    }
};

export const proxyFetchData = () => dispatch => {
    dispatch(proxyFetchDataIsLoading(true));
    fetch('/api/proxy')
        .then(response => {
            dispatch(proxyFetchDataIsLoading(false));
            return checkResponse(response);
        })
        .then(response => response.json())
        .then(proxies => {
            dispatch(proxyFetchDataSuccess(proxies));
            dispatch(proxySelectProxy(proxies[0]))
        })
        .catch(error => {
            console.error(error);
            dispatch(proxyFetchDataHasErrored(true))
        })
};