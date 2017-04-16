import React from "react";

const Quality = props => {
    if (props.quality === 'VIP') {
        return (
            <i className="icon-vip-1 vip"/>
        );
    }
    else if (props.quality === 'Trusted') {
        return (
            <i className="icon-trusted-1 trust"/>
        );
    }
    else {
        return (
            <i className="icon-unknown-1 unknown"/>
        );
    }
};

export default Quality;