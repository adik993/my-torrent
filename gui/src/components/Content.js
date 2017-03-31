import React from "react";
import {connect} from "react-redux";
import User from "components/User";

class Content extends React.Component {
    render() {
        return (
            <div>
                <User/>
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