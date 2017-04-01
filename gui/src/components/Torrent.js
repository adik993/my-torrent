import React from "react";
import {connect} from "react-redux";
import {torrentsSelectTorrent} from "actions/torrents";
import Size from "components/Size";
import PublishDate from "components/PublishDate";
import User from "components/User";
import Quality from "components/Quality";
import MagnetLink from "components/MagnetLink";
import Chosen from "components/Chosen";

class Torrent extends React.Component {

    select = selected => {
        this.props.selectTorrent(this.props.torrent, selected);
    };

    handleMagnetClick = () => {
        this.select(true);
    };

    handleUnchoose = () => {
        this.select(false);
    };

    render() {
        let torrent = this.props.torrent;
        return (
            <tr>
                <td>
                    <Chosen chosen={torrent.chosen} onUnchoose={this.handleUnchoose}/>
                </td>
                <td>
                    {torrent.title}
                </td>
                <td>
                    <MagnetLink onMagnetClick={this.handleMagnetClick} magnetLink={torrent.magnetLink}/>
                </td>
                <td>
                    {torrent.category}
                </td>
                <td>
                    <User user={torrent.user}/>
                </td>
                <td>
                    <PublishDate date={torrent.publishDate}/>
                </td>
                <td>
                    <Quality quality={torrent.quality}/>
                </td>
                <td>
                    <Size size={torrent.size}/>
                </td>
                <td>
                    {torrent.leeches}
                </td>
                <td>
                    {torrent.seeds}
                </td>
            </tr>
        );
    }
}

const mapStateToProps = state => {
    return {}
};

const mapDispatchToProps = dispatch => {
    return {
        selectTorrent: (torrent, selected) => dispatch(torrentsSelectTorrent(torrent, selected))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Torrent);