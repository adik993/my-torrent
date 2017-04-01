import React from "react";
import {connect} from "react-redux";
import SelectField from "material-ui/SelectField";
import MenuItem from "material-ui/MenuItem";
import {proxyFetchData, proxySelectProxy} from "actions/proxy";

class ProxySelect extends React.Component {

    componentDidMount() {
        this.props.fetchProxies();
    }

    handleChange = (event, index, value) => {
        this.props.selectProxy(value);
    };

    render() {
        return (
            <SelectField
                className="search-select col-sm-4"
                style={{width: null}}
                value={this.props.selectedProxy}
                onChange={this.handleChange}
                maxHeight={300}
            >
                {this.props.proxies.map(proxy =>
                    <MenuItem
                        value={proxy}
                        key={proxy.id}
                        primaryText={proxy.domain}
                    />
                )}
            </SelectField>
        );
    }
}

const mapStateToProps = state => {
    return {
        proxies: state.proxies,
        selectedProxy: state.selectedProxy
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchProxies: () => dispatch(proxyFetchData()),
        selectProxy: proxy => dispatch(proxySelectProxy(proxy))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ProxySelect);