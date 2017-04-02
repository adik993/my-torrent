import React from "react";
import {connect} from "react-redux";
import TextField from "material-ui/TextField";
import ProviderSelect from "components/ProviderSelect";
import {torrentsFetchData} from "actions/torrents";

class SearchBox extends React.Component {

    constructor(props) {
        super(props);
        this.state = {query: 'Flash S01E01'}
    }

    onSearch = (event) => {
        event.preventDefault();
        this.props.search(this.state.query, this.props.provider);
    };

    onChange = event => {
        let query = event.target.value;
        this.setState({query})
    };

    render() {
        return (

            <form className="col-xs-12 text-center" onSubmit={this.onSearch}>
                <div>
                    <TextField
                        className="search-input col-sm-8"
                        style={{width: null}}
                        hintText="Search..."
                        value={this.state.query}
                        onChange={this.onChange}
                    />
                    <ProviderSelect />
                </div>
            </form>

        );
    }
}

const mapStateToProps = state => {
    return {
        provider: state.selectedProvider
    }
};

const mapDispatchToProps = dispatch => {
    return {
        search: (query, provider) => dispatch(torrentsFetchData(query, provider))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(SearchBox);