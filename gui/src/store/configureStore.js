import {applyMiddleware, createStore} from "redux";
import {composeWithDevTools} from "redux-devtools-extension";
import thunk from "redux-thunk";
import rootReducer from "reducers";
import {createReduxSse} from "sse";

console.log(process.env.NODE_ENV);

const configureStore = initialState => {
    return createStore(
        rootReducer,
        initialState,
        process.env.NODE_ENV === 'dev' ? composeWithDevTools(applyMiddleware(thunk, createReduxSse())) : applyMiddleware(thunk, createReduxSse())
    );
};
export default configureStore;