import React from "react";
import {Provider} from "react-redux";
import configureStore from "store/configureStore";
import Content from "components/Content";

const store = configureStore();

class App extends React.Component {
    render() {
        return (
            <Provider store={store}>
                <Content/>
            </Provider>
        )
    }
}


export default App;