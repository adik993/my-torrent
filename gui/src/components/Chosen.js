import React from "react";

const Chosen = props => {
    return (
        <input
            type="checkbox"
            checked={props.chosen}
            disabled={!props.chosen}
            onChange={props.onUnchoose}
        />
    );
};

export default Chosen;