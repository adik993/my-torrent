import React from "react";
import {connect} from "react-redux";
import SelectField from "material-ui/SelectField";
import MenuItem from "material-ui/MenuItem";
import {
    providerFetchData,
    providerSelectProvider,
    providerSubscribeProviders,
    providerUnsubscribeProviders
} from "actions/provider";

class ProviderSelect extends React.Component {

    componentDidMount() {
        this.props.fetchProviders();
        this.props.subscribeProviders();
    }

    componentWillUnmount() {
        this.props.unSubscribeProviders();
    }

    handleChange = (event, index, value) => {
        this.props.selectProvider(value);
    };

    render() {
        return (
            <SelectField
                className="search-select col-sm-4"
                style={{width: null}}
                value={this.props.selectedProvider}
                onChange={this.handleChange}
                maxHeight={300}
            >
                {this.props.providers.map(provider =>
                    <MenuItem
                        value={provider.id}
                        key={provider.id}
                        primaryText={provider.name}
                        secondaryText={provider.up ? 'UP' : 'DOWN'}
                    />
                )}
            </SelectField>
        );
    }
}

const mapStateToProps = state => {
    return {
        providers: state.providers,
        selectedProvider: state.selectedProvider
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchProviders: () => dispatch(providerFetchData()),
        selectProvider: provider => dispatch(providerSelectProvider(provider)),
        subscribeProviders: () => dispatch(providerSubscribeProviders()),
        unSubscribeProviders: () => dispatch(providerUnsubscribeProviders())
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ProviderSelect);