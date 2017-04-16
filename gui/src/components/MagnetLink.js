import React from "react";

const MagnetLink = props => {
    return (
        <div>
            <a onClick={props.onMagnetClick} href={props.magnetLink}>
                 <i className="icon-magnet-1 magnet magnets-bitch"/>
            </a>
        </div>
    );
};

export default MagnetLink;
