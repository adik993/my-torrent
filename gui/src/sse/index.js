export const SSE_SUBSCRIBE = "SSE_SUBSCRIBE"
export const SSE_UNSUBSCRIBE = "SSE_UNSUBSCRIBE"

export const sseSubscribe = (url, converter = body => body) => {
    return {
        type: SSE_SUBSCRIBE,
        url,
        converter
    }
};

export const sseUnsubscribe = url => {
    return {
        type: SSE_UNSUBSCRIBE,
        topic: url
    }
};

export const createReduxSse = () => {
    const subscriptions = new Map()

    return store => next => action => {
        try {
            switch (action.type) {
                case SSE_SUBSCRIBE:
                    let source = new EventSource(action.url)
                    source.onmessage = event => store.dispatch(action.converter(JSON.parse(event.data)))
                    subscriptions.set(action.url, source)
                    break
                case SSE_UNSUBSCRIBE:
                    subscriptions.get(action.url).close()
                    subscriptions.delete(action.url)
            }
        } finally {
            next(action)
        }
    }
}