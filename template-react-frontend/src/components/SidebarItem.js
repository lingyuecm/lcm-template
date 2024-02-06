import "./SidebarItem.css";
import {useLocation, useNavigate} from "react-router-dom";
import {KeyboardArrowUp} from "@mui/icons-material";
import {useDispatch, useSelector} from "react-redux";
import {appendExpandedKey, removeExpandedKey, setCurrentMenuPath} from "../store/menuSlice";
import {useEffect} from "react";

export default function SidebarItem(props) {
    const location = useLocation();
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const sidebarExpanded = useSelector(state => state.sidebar.sidebarExpanded);
    let expandedKeys = useSelector(state => state.menu.expandedKeys);
    let currentMenuPath = useSelector(state => state.menu.currentMenuPath);
    const expandArrowSize = "1.8rem";
    function getElementKey(indices) {
        return indices.join("-");
    }

    function onMenuItemClick(path) {
        if (path === currentMenuPath) {
            return;
        }
        dispatch(setCurrentMenuPath(path));
        navigate(path, {
            state: {
                from: path
            }
        });
    }

    function onSubmenuClick(elementKey) {
        if (expandedKeys.includes(elementKey)) {
            /*
            The submenu is expanded
            Make it folded
             */
            dispatch(removeExpandedKey(elementKey));
        }
        else {
            /*
            The submenu is folded
            Make it expanded
             */
            dispatch(appendExpandedKey(elementKey));
        }
    }

    useEffect(() => {
        dispatch(setCurrentMenuPath(location.pathname));
    }, []);

    return <div style={{display: "block"}}>
        {
            props.items.map((menu, index) => {
                const elementKey = getElementKey([props.parentKey, index]);

                if (sidebarExpanded) {
                    if (menu.children) {
                        return [
                            <div className={"Sidebar-menu-item"} style={{
                                textIndent: props.indent + "ch"
                            }} key={elementKey} onClick={() => onSubmenuClick(elementKey)}>
                                { menu.title }
                                <KeyboardArrowUp style={expandedKeys.includes(elementKey) ? {
                                    width: expandArrowSize,
                                    height: expandArrowSize,
                                    marginLeft: "1rem"
                                } : {
                                    width: expandArrowSize,
                                    height: expandArrowSize,
                                    marginLeft: "1rem",
                                    transform: "rotateX(180deg)"
                                }}/>
                            </div>,
                            <div className={expandedKeys.includes(elementKey) ? "Submenu-wrapper" : "Submenu-wrapper Submenu-wrapper-hidden"}>
                                <SidebarItem items={menu.children} indent={props.indent + 2} parentKey={elementKey}/>
                            </div>
                        ]
                    }
                    else {
                        return <div className={(menu.path === currentMenuPath || menu.path === location.state.from) ?
                            "Sidebar-menu-item Current-item" : "Sidebar-menu-item"} style={{
                            textIndent: props.indent + "ch"
                        }} key={elementKey}
                                    onClick={() => onMenuItemClick(menu.path)}>
                            { menu.title }
                        </div>
                    }
                }
                else {
                    // Display the menus on the first level only
                    return <div className={"Sidebar-menu-item Sidebar-item-abbreviation"}>
                        <div className={"Sidebar-menu-item-abbreviation-text"}>{menu.title[0]}</div>
                    </div>
                }
            })
        }
    </div>
}
