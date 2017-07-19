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
            <tr className="">
                <td className="">
                    <Chosen chosen={torrent.chosen} onUnchoose={this.handleUnchoose}/>
                </td>
                <td className="col-sm-12 col-md-7">
                    <a className="hover-fill" data-txthover={torrent.title} href={torrent.torrentLink}>{torrent.title}</a>
                    <ul className="title-data">
                        <li> <PublishDate date={torrent.publishDate}/>  </li>
                        <li> <Quality quality={torrent.quality}/> </li>
                        <li> <User user={torrent.user}/> </li>
                        <li className="hidden-md hidden-lg"> {torrent.category} </li>
                        <li className="hidden-md hidden-lg"> <Size size={torrent.size}/> </li>
                        <li className="hidden-md hidden-lg">L: {torrent.leeches} </li>
                        <li className="hidden-md hidden-lg">S: {torrent.seeds} </li>

                    </ul>
                </td>
                <td className="col-sm-12 col-md-1 text-center visible-md visible-lg">
                    <MagnetLink onMagnetClick={this.handleMagnetClick} magnetLink={torrent.magnetLink}/>
                </td>
                <td className="col-sm-12 col-md-1 text-center category visible-md visible-lg">
                    {torrent.category}
                </td>
                <td className="col-sm-12 col-md-1 text-center visible-md visible-lg">
                    <Size size={torrent.size}/>
                </td>
                <td className="col-sm-12 col-md-1 text-center leeches visible-md visible-lg">
                    {torrent.leeches}
                </td>
                <td className="col-sm-12 col-md-1 text-center seeds visible-md visible-lg">
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