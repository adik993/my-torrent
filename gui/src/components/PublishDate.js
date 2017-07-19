import React from "react";
import moment from "moment";

const PublishDate = props => {
    return (
        <div>
            {moment(props.date).format('YYYY-MM-DD')}
        </div>
    );
};

export default PublishDate;
