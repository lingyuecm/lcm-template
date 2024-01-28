import React from "react";
import './Dashboard.css';
import {getAccessToken} from "../../utils/cacheManager";

export default class Dashboard extends React.Component {
    render() {
        return (
            <div>
                <p>I don't know what should be displayed in this page for such a simple RBAC system,
                    but I feel there should be a dashboard,
                    so I'd like to display the access token below</p>
                <p>{ getAccessToken() }</p>
            </div>
        )
    }
}
