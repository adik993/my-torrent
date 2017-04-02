import {combineReducers} from "redux";
import {providerHasErrored, providerIsLoading, providers, selectedProvider} from "reducers/provider";
import {torrents, torrentsHasErrored, torrentsIsLoading} from "reducers/torrents";

export default combineReducers({
    providers,
    providerIsLoading,
    providerHasErrored,
    selectedProvider,
    torrentsIsLoading,
    torrentsHasErrored,
    torrents
});