import SockJS from "sockjs-client";
import Stomp from "stompjs";

const WS_CONNECT = 'WS_CONNECT';
const WS_CONNECT_SUCCESS = 'WS_CONNECT_SUCCESS';
const WS_DISCONNECT = 'WS_DISCONNECT';
const WS_SUBSCRIBE = 'WS_SUBSCRIBE';
const WS_UNSUBSCRIBE = 'WS_UNSUBSCRIBE';

export const wsConnect = () => {
    return {
        type: WS_CONNECT
    }
};

export const wsConnectSuccess = () => {
    return {
        type: WS_CONNECT_SUCCESS
    }
};

export const wsDisconnect = () => {
    return {
        type: WS_DISCONNECT
    }
};

export const wsSubscribe = (topic, converter = body => body) => {
    return {
        type: WS_SUBSCRIBE,
        topic,
        converter
    }
};

export const wsUnsubscribe = topic => {
    return {
        type: WS_UNSUBSCRIBE,
        topic
    }
};

export const createReduxStomp = () => {
    const socket = SockJS('/gs-guide-websocket');
    const stomp = Stomp.over(socket);
    let connected = false;
    const subscriptions = new Map();

    const subscribe = store => topic => {
        let sub = stomp.subscribe(topic, onMessage(store));
        let tmp = subscriptions.get(topic);
        subscriptions.set(topic, {...tmp, sub});
    };

    const onMessage = store => frame => {
        let topic = frame.headers.destination;
        let converter = subscriptions.get(topic).converter;
        store.dispatch(converter(JSON.parse(frame.body)));
    };

    return store => next => action => {
        try {
            let topic;
            let sub;
            switch (action.type) {
                case WS_CONNECT:
                    stomp.connect({}, frame => {
                        connected = true;
                        store.dispatch(wsConnectSuccess());
                        subscriptions.forEach((value, key) => {
                            if (!value.sub) subscribe(store)(key);
                        })
                    });
                    break;
                case WS_DISCONNECT:
                    stomp.disconnect();
                    break;
                case WS_SUBSCRIBE:
                    topic = action.topic;
                    let converter = action.converter;
                    if (connected) {
                        subscribe(store)(topic)
                    } else {
                        subscriptions.set(topic, {converter});
                    }
                    break;
                case WS_UNSUBSCRIBE:
                    topic = action.topic;
                    sub = subscriptions.get(topic);
                    if (sub) {
                        sub.unsubscribe();
                        subscriptions.delete(topic);
                    }
                    break;
            }
        } finally {
            next(action);
        }
    };
};