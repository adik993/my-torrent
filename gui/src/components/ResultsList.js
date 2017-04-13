import React from "react";
import {connect} from "react-redux";
import Torrent from "components/Torrent";

class ResultsList extends React.Component {
    render() {
        if (this.props.isLoading) {
            return (<h1>Searching...</h1>)
        }
        if (this.props.hasErrored) {
            return (<h1>Error</h1>)
        }
        if (this.props.torrents.length == 0) {
            return (<h1>No content</h1>)
        }
        return (
            <div className="text-left tabela">
                <table className="table-hover">
                    <thead>
                    <tr>
                        <th>

                        </th>
                        <th className="col-sm-12 col-md-6">
                            Title
                        </th>
                        <th className="col-md-1 text-center">
                            Link
                        </th>
                        <th className="col-md-1 text-center">
                            Category
                        </th>
                        <th className="col-md-1 text-center">
                            Size
                        </th>
                        <th className="col-md-1 text-center">
                            Leech
                        </th>
                        <th className="col-md-1 text-center">
                            Seed
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.props.torrents.map(torrent =>
                        <Torrent key={torrent.id} torrent={torrent}/>
                    )}
                    </tbody>
                </table>
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
    return {}
};

export default connect(mapStateToProps, mapDispatchToProps)(ResultsList);