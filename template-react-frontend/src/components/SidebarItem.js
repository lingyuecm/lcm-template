import {useLocation, useNavigate} from "react-router-dom";
import {KeyboardArrowUp} from "@mui/icons-material";
import {useDispatch, useSelector} from "react-redux";
import {appendExpandedKey, removeExpandedKey, setCurrentMenuPath} from "../store/menuSlice";
import {useEffect} from "react";
import styled from "styled-components";
import {colorBlueDark, colorBlueDarker, colorGreyD2, colorOrange} from "../utils/constant";

const SidebarItemBase = styled.div`
    display: flex;
    width: 100%;
    height: 5rem;
    font-size: 1.5rem;
    text-indent: ${props => props.indent + "ch"};
    align-items: center;
    overflow-x: hidden;
    &:hover {
        cursor: pointer;
        background-color: ${() => colorBlueDark};
    }
`

const SubmenuWrapper = styled.div`
    width: 100%;
    height: ${props => {
        return props.parentExpanded ? "auto" : "0"
    }};
    overflow-y: hidden;
    transition-duration: 0.5s;
`

const SidebarItemLeaf = styled(SidebarItemBase)`
    color: ${props => {
        return props.current ? colorOrange : "inherit"
    }};
    font-weight: ${props => {
        return props.current ? 1000 : "inherit"
    }};
    background-color: ${props => {
        return props.current ? colorBlueDarker : "inherit"
    }};
    &:hover {
        cursor: pointer;
        background-color: ${props => {
            return props.current ? colorBlueDarker : colorBlueDark
        }};
    }
`

const SidebarItemAbbreviation = styled(SidebarItemBase)`
    color: ${() => colorGreyD2};
    font-size: 2rem;
    font-weight: 1000;
`

const SidebarItemAbbreviationText = styled.div`
    display: block;
    width: 100%;
    text-align: center;
    opacity: 80%;
`

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
                            <SidebarItemBase
                                indent={props.indent}
                                key={elementKey} onClick={() => onSubmenuClick(elementKey)}>
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
                            </SidebarItemBase>,
                            <SubmenuWrapper parentExpanded={expandedKeys.includes(elementKey)}>
                                <SidebarItem items={menu.children} indent={props.indent + 2} parentKey={elementKey}/>
                            </SubmenuWrapper>
                        ]
                    }
                    else {
                        return <SidebarItemLeaf current={(menu.path === currentMenuPath || menu.path === location.state.from)} indent={props.indent}
                            key={elementKey}
                            onClick={() => onMenuItemClick(menu.path)}>
                            { menu.title }
                        </SidebarItemLeaf>
                    }
                }
                else {
                    // Display the menus on the first level only
                    return <SidebarItemAbbreviation>
                        <SidebarItemAbbreviationText>{menu.title[0]}</SidebarItemAbbreviationText>
                    </SidebarItemAbbreviation>
                }
            })
        }
    </div>
}
