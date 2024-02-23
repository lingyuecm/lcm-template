import {useLocation, useNavigate} from "react-router-dom";
import {KeyboardArrowUp} from "@mui/icons-material";
import {appendExpandedKey, Menu, removeExpandedKey, setCurrentMenuPath} from "../../store/menuSlice";
import React, {useEffect} from "react";
import styled from "styled-components";
import {colorBlueDark, colorBlueDarker, colorGreyD2, colorOrange} from "../../utils/constant";
import {useAppDispatch, useAppSelector} from "../../store";

type SidebarItemBaseProps = {
    indent: number
}
const SidebarItemBase = styled.div<SidebarItemBaseProps>`
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

type SubmenuWrapperProps = {
    parentExpanded: boolean
}
const SubmenuWrapper = styled.div<SubmenuWrapperProps>`
    width: 100%;
    height: ${props => {
        return props.parentExpanded ? "auto" : "0"
    }};
    overflow-y: hidden;
    transition-duration: 0.5s;
`

type SidebarItemLeafProps = {
    current: boolean
}
const SidebarItemLeaf = styled(SidebarItemBase)<SidebarItemLeafProps>`
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
            return props.current ? colorBlueDarker : colorBlueDark;
        }};
    }
`

const SidebarItemAbbreviation = styled(SidebarItemBase)`
    color: ${() => {
        return colorGreyD2;
    }};
    font-size: 2rem;
    font-weight: 1000;
    text-indent: 0;
`

const SidebarItemAbbreviationText = styled.div`
    display: block;
    width: 100%;
    text-align: center;
    opacity: 80%;
`

export default function SidebarItem(props: {
    items: Menu[],
    parentKey: string | number,
    indent: number}): React.ReactElement {

    const location = useLocation();
    const navigate = useNavigate();
    const dispatch = useAppDispatch();
    const sidebarExpanded = useAppSelector(state => state.sidebar.sidebarExpanded);
    let expandedKeys = useAppSelector(state => state.menu.expandedKeys);
    let currentMenuPath = useAppSelector(state => state.menu.currentMenuPath);
    const expandArrowSize: string = "1.8rem";
    function getElementKey(indices: any[]): string {
        return indices.join("-");
    }

    function onMenuItemClick(path: string):void {
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

    function onSubmenuClick(elementKey: string): void {
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
                    return <SidebarItemAbbreviation indent={2}>
                        <SidebarItemAbbreviationText>{menu.title[0]}</SidebarItemAbbreviationText>
                    </SidebarItemAbbreviation>
                }
            })
        }
    </div>
}
