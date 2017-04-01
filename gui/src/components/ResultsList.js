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
            <div className="text-left">
                <table>
                    <thead>
                    <tr>
                        <th>
                            Chosen
                        </th>
                        <th>
                            Title
                        </th>
                        <th>
                            Magnet Link
                        </th>
                        <th>
                            Category
                        </th>
                        <th>
                            User
                        </th>
                        <th>
                            Publish date
                        </th>
                        <th>
                            Quality
                        </th>
                        <th>
                            Size
                        </th>
                        <th>
                            Leech
                        </th>
                        <th>
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