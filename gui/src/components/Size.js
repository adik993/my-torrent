import React from "react";
import {formatFileSize} from "utils";

const Size = props => {
    return (
        <div>{formatFileSize(props.size, true)}</div>
    );
};

export default Size;
