import {checkResponse} from "utils";
import * as types from "actions/types";
import {wsSubscribe, wsUnsubscribe} from "redux-stomp";

export const providerFetchDataIsLoading = isLoading => {
    return {
        type: types.PROVIDER_FETCH_DATA_IS_LOADING,
        isLoading
    }
};

export const providerFetchDataHasErrored = hasErrored => {
    return {
        type: types.PROVIDER_FETCH_DATA_HAS_ERRORED,
        hasErrored
    };
};

export const providerFetchDataSuccess = providers => {
    return {
        type: types.PROVIDER_FETCH_DATA_SUCCESS,
        providers
    }
};

export const providerSelectProvider = selectedProvider => {
    return {
        type: types.PROVIDER_SELECT_PROVIDER,
        selectedProvider
    }
};

export const providerFetchData = () => dispatch => {
    dispatch(providerFetchDataIsLoading(true));
    fetch('/api/providers')
        .then(response => {
            dispatch(providerFetchDataIsLoading(false));
            return checkResponse(response);
        })
        .then(response => response.json())
        .then(providers => {
            dispatch(providerFetchDataSuccess(providers));
            dispatch(providerSelectProvider(providers[0].id))
        })
        .catch(error => {
            console.error(error);
            dispatch(providerFetchDataHasErrored(true))
        })
};

export const providerSubscribeProviders = () => dispatch => {
    dispatch(wsSubscribe('/client/providers', body => {
        return {
            type: types.PROVIDER_FETCH_DATA_SUCCESS,
            providers: body
        }
    }));
};

export const providerUnsubscribeProviders = () => dispatch => {
    dispatch(wsUnsubscribe('/client/providers'));
};