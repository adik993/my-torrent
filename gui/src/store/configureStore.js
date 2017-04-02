import {applyMiddleware, createStore} from "redux";
import {composeWithDevTools} from "redux-devtools-extension";
import thunk from "redux-thunk";
import rootReducer from "reducers";
import {createReduxStomp} from "redux-stomp";
// import { startReduxClient, actionCreator, reduxSockjs, createReducer } from 'redux-sockjs'
// import SockJS from "sockjs-client";
// import Stomp from "stompjs";
//
// const socket = SockJS('/gs-guide-websocket');
// const stomp = Stomp.over(socket);
// stomp.connect({}, frame => {
//     console.log(frame);
//     stomp.subscribe('/client/providers', message => {
//         console.log(message);
//     });
// });

console.log(process.env.NODE_ENV);

const configureStore = initialState => {
    return createStore(
        rootReducer,
        initialState,
        process.env.NODE_ENV === 'dev' ? composeWithDevTools(applyMiddleware(thunk, createReduxStomp())) : applyMiddleware(thunk, createReduxStomp())
    );
};
export default configureStore;