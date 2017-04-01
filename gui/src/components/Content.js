import React from "react";
import {connect} from "react-redux";
import getMuiTheme from 'material-ui/styles/getMuiTheme'
import User from "components/User";
import SearchBox from 'components/SearchBox';
import ResultsList from 'components/ResultsList';

class Content extends React.Component {

    static childContextTypes = {
        muiTheme: React.PropTypes.object
    };

    getChildContext() {
        return {
            muiTheme: getMuiTheme()
        }
    }

    render() {
        return (
            <div className="container">
                <SearchBox/>
                <ResultsList/>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {}
};

const mapDispatchToProps = dispatch => {
    return {}
};

export default connect(mapStateToProps, mapDispatchToProps)(Content);