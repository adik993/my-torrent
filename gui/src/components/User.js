import React from "react";

const User = props => {
    return (
        <div>
            {props.user ? props.user : <i>Anonymous</i>}
        </div>
    );
};


export default User;