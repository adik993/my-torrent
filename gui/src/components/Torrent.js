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
                <td className="">
                    <Chosen chosen={torrent.chosen} onUnchoose={this.handleUnchoose}/>
                </td>
                <td className="col-sm-12 col-md-7">
                    <a className="hover-fill" data-txthover={torrent.title} href={torrent.torrentLink}>{torrent.title}</a>
                    <ul className="title-data">
                        <li> <PublishDate date={torrent.publishDate}/>  </li>
                        <li> <Quality quality={torrent.quality}/> </li>
                        <li> <User user={torrent.user}/> </li>
                    </ul>
                </td>
                <td className="col-sm-12 col-md-1 text-center">
                    <MagnetLink onMagnetClick={this.handleMagnetClick} magnetLink={torrent.magnetLink}/>
                </td>
                <td className="col-sm-12 col-md-1 text-center category">
                    {torrent.category}
                </td>
                <td className="col-sm-12 col-md-1 text-center">
                    <Size size={torrent.size}/>
                </td>
                <td className="col-sm-12 col-md-1 text-center leeches">
                    {torrent.leeches}
                </td>
                <td className="col-sm-12 col-md-1 text-center seeds">
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