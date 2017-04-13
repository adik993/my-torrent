import React from "react";

const Chosen = props => {
    return (
        <div className="coloured">
            <div className="checkbox ">
                <label>
                    <input
                        type="checkbox"
                        checked={props.chosen}
                        disabled={!props.chosen}
                        onChange={props.onUnchoose}/>
                    <span className="checkbox-material">
                        <span className="check"></span>
                    </span>
                </label>
            </div>
        </div>

    );
};

export default Chosen;


