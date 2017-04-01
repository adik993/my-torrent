import React from 'react';
import {connect} from 'react-redux';
import TextField from 'material-ui/TextField';
import Paper from 'material-ui/Paper';
import ProxySelect from 'components/ProxySelect';
import {torrentsFetchData} from 'actions/torrents';

class SearchBox extends React.Component {

    constructor(props) {
        super(props);
        this.state = {query: ''}
    }

    onSearch = (event) => {
        event.preventDefault()
        this.props.search(this.state.query, this.props.proxy);
    };

    onChange = event => {
        let query = event.target.value;
        this.setState({query})
    };

    render() {
        return (
            <Paper zDepth={2} className="search-box">
                <form
                    onSubmit={this.onSearch}>

                    <TextField
                        className="search-input"
                        hintText="Search..."
                        value={this.state.query}
                        onChange={this.onChange}
                    />
                    <ProxySelect />
                </form>
            </Paper>
        );
    }
}

const mapStateToProps = state => {
    return {
        proxy: state.selectedProxy
    }
};

const mapDispatchToProps = dispatch => {
    return {
        search: (query, proxy) => dispatch(torrentsFetchData(query, proxy))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(SearchBox);