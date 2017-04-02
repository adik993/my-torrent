import React from "react";
import {connect} from "react-redux";
import getMuiTheme from "material-ui/styles/getMuiTheme";
import Paper from "material-ui/Paper";
import SearchBox from "components/SearchBox";
import ResultsList from "components/ResultsList";
import {wsConnect, wsDisconnect} from "redux-stomp";

class Content extends React.Component {

    static childContextTypes = {
        muiTheme: React.PropTypes.object
    };

    getChildContext() {
        return {
            muiTheme: getMuiTheme()
        }
    }

    componentDidMount() {
        this.props.connectToWs();
    }

    componentWillUnmount() {
        this.props.disconnectFromWs();
    }

    render() {
        return (
            <Paper className="container" zDepth={2}>
                <div className="text-center">
                    <SearchBox/>
                    <ResultsList/>
                </div>
            </Paper>
        );
    }
}

const mapStateToProps = state => {
    return {}
};

const mapDispatchToProps = dispatch => {
    return {
        connectToWs: () => dispatch(wsConnect()),
        disconnectFromWs: () => dispatch(wsDisconnect())
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Content);