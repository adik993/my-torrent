import React from "react";

const MagnetLink = props => {
    return (
        <div>
            <a onClick={props.onMagnetClick} href={props.magnetLink}>
                <i className="glyphicon glyphicon-magnet" aria-hidden="true"/>
            </a>
        </div>
    );
};

export default MagnetLink;
