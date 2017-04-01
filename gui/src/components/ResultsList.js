import React from 'react';
import {connect} from 'react-redux';

class ResultsList extends React.Component {
    render() {
        if(this.props.isLoading) {
            return (<h1>Searching...</h1>)
        }
        if(this.props.hasErrored) {
            return (<h1>Error</h1>)
        }
        return (
            <div>
                <ul>
                    {this.props.torrents.map(torrent =>
                        <li>{torrent.title}</li>
                    )}
                </ul>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        isLoading: state.torrentsIsLoading,
        hasErrored: state.torrentsHasErrored,
        torrents: state.torrents
    }
};

const mapDispatchToProps = dispatch => {
    return {

    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ResultsList);