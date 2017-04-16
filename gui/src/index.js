import React from "react";
import ReactDOM from "react-dom";
import injectTapEventPlugin from 'react-tap-event-plugin';
import App from "components/App";
import "bootstrap-sass/assets/stylesheets/_bootstrap.scss";
import "bootstrap-sass/assets/javascripts/bootstrap";
import "styles/my-torrent.css";
import "fonts/my-torrent.eot";
import "styles/screen.scss";
injectTapEventPlugin();

ReactDOM.render(<App/>, document.getElementById('app'));
