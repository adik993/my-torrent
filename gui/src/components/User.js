import React from "react";
import {connect} from "react-redux";
import {userFetchData} from "actions/user";

class User extends React.Component {

    componentDidMount() {
        this.props.fetchUser();
    }

    render() {
        if (this.props.hasErrored) {
            return <p>Error fetching user information</p>
        }
        if (this.props.isLoading) {
            return <p>Loading...</p>
        }

        return (
            <div>
                <h1>Hello World</h1>
                <table>
                    <thead>
                    <tr>
                        <td>Name</td>
                        <td>Value</td>
                    </tr>
                    </thead>
                    <tbody>
                    {this.props.user && this.props.user.map(u =>
                        [
                            <tr>
                                <td>Domain</td>
                                <td>{u.domain}</td>
                            </tr>,
                            <tr>
                                <td>Country</td>
                                <td>{u.country}</td>
                            </tr>
                        ]
                    )}
                    </tbody>
                </table>
            </div>
        )
    }
}

const mapStateToProps = state => {
    return {
        isLoading: state.userIsLoading,
        hasErrored: state.userHasErrored,
        user: state.user
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchUser: () => dispatch(userFetchData())
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(User);