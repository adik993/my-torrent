import {combineReducers} from "redux";
import {user, userHasErrored, userIsLoading} from "reducers/user";

export default combineReducers({
    userIsLoading,
    userHasErrored,
    user
});