import {combineReducers} from "redux";
import {proxies, proxyHasErrored, proxyIsLoading, selectedProxy} from "reducers/proxy";
import {torrents, torrentsHasErrored, torrentsIsLoading} from "reducers/torrents";

export default combineReducers({
    proxies,
    proxyIsLoading,
    proxyHasErrored,
    selectedProxy,
    torrentsIsLoading,
    torrentsHasErrored,
    torrents
});