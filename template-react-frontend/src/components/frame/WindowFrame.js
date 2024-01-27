import React from "react";
import './WindowFrame.css';
import {ArrowForward} from "@mui/icons-material";
import {Routes} from "react-router-dom";
import {createRoute} from "../../rounter";

export default class WindowFrame extends React.Component {
    constructor(props) {
        super(props)

        this.onSidebarToggle = this.onSidebarToggle.bind(this);
    }
    state = {
        sidebarExpanded: false
    }
    styles = {
        sidebarToggleArrow: {
            width: '2vw',
            height: '2vw',
            margin: '0.5vw',
            color: '#E7A158'
        }
    }
    render() {
        return (
            <div className={"Root-window"}>
                <div className={this.state.sidebarExpanded ? "Sidebar" : "Sidebar Sidebar-folded"}>
                    <div className={this.state.sidebarExpanded ? "Sidebar-toggle-button Sidebar-toggle-button-flipped" : "Sidebar-toggle-button"} onClick={this.onSidebarToggle}>
                        <ArrowForward style={this.styles.sidebarToggleArrow}/>
                    </div>
                </div>
                <div className={this.state.sidebarExpanded ? "Content" : "Content Content-wide"}>
                    <Routes>
                        { this.props.children.map((child, index) => createRoute(child, index)) }
                    </Routes>
                </div>
            </div>
        )
    }

    onSidebarToggle() {
        const expanded = this.state.sidebarExpanded;
        this.setState({
            sidebarExpanded: !expanded
        })
    }
}
