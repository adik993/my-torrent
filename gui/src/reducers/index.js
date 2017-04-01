import {combineReducers} from "redux";
import {user, userHasErrored, userIsLoading} from "reducers/user";
import {
    proxies,
    proxyIsLoading,
    proxyHasErrored,
    selectedProxy
} from 'reducers/proxy';
import {
    torrentsIsLoading,
    torrentsHasErrored,
    torrents
} from 'reducers/torrents';

export default combineReducers({
    userIsLoading,
    userHasErrored,
    user,
    proxies,
    proxyIsLoading,
    proxyHasErrored,
    selectedProxy,
    torrentsIsLoading,
    torrentsHasErrored,
    torrents
});