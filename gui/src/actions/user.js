import * as types from "actions/types";
import {checkResponse} from "utils";

export const userIsLoading = isLoading => {
    return {
        type: types.USER_IS_LOADING,
        isLoading
    }
};

export const userHasErrored = hasErrored => {
    return {
        type: types.USER_HAS_ERRORED,
        hasErrored
    }
};

export const userFetchDataSuccess = user => {
    return {
        type: types.USER_FETCH_DATA_SUCCESS,
        user
    }
};

export const userFetchData = () => dispatch => {
    dispatch(userIsLoading(true));
    fetch('/api/proxy')
        .then(response => checkResponse(response))
        .then(response => response.json())
        .then(user => {
            dispatch(userFetchDataSuccess(user));
            dispatch(userIsLoading(false));
        })
        .catch(error => {
            console.log(error);
            dispatch(userHasErrored(true))
        });
};