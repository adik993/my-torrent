import {applyMiddleware, createStore} from "redux";
import {composeWithDevTools} from "redux-devtools-extension";
import thunk from "redux-thunk";
import rootReducer from "reducers";


console.log(process.env.NODE_ENV);

const configureStore = initialState => {
    return createStore(
        rootReducer,
        initialState,
        process.env.NODE_ENV === 'dev' ? composeWithDevTools(applyMiddleware(thunk)) : applyMiddleware(thunk)
    );
};
export default configureStore;