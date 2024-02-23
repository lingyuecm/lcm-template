import {ArrowForward, Key, Refresh} from "@mui/icons-material";
import {Routes} from "react-router-dom";
import {createRoute, RouterModel} from "../../router";
import {setSidebarExpanded} from "../../store/sidebarSlice";
import React, {useEffect, useState} from "react";
import {setPersonName} from "../../store/personNameSlice";
import SidebarItem from "./SidebarItem";
import styled, {keyframes} from "styled-components";
import {
    colorBlue,
    colorBlueDark,
    colorError,
    colorOrange,
    colorOrangeDark,
    colorOrangeDarker, colorSuccess
} from "../../utils/constant";
import {createTheme, ThemeProvider} from "@mui/material";
import {useAppDispatch, useAppSelector} from "../../store";
import {getAccessToken, removeAccessToken} from "../../utils/cacheManager";
import ToastUtils from "../../utils/ToastUtils";
import {logoutApi, metadataApi} from "../../api/userApi";
import {refreshPermissionsApi} from "../../api/permissionApi";

const RootWindow = styled.div`
    display: flex;
    width: 100%;
    height: 100vh;
    word-wrap: break-word;
`

type SidebarProps = {
    expanded: boolean
}
const Sidebar = styled.div<SidebarProps>`
    display: inline-block;
    width: ${props => {
        return props.expanded ? "30rem" : "5rem"
    }};
    height: 100%;
    background-color: ${() => {
        return colorBlue;
    }};
    box-shadow: 0.1rem 0 0.2rem #666666;
    transition-duration: 0.5s;
    z-index: 100;
`

const SidebarToggleButtonWrapper = styled.div`
    width: 100%;
    height: 5rem;
`

const refreshing = keyframes`
    from {
        transform: rotateZ(0deg);
    }
    to {
        transform: rotateZ(360deg);
    }
`

type RefreshPermissionsButtonProps = {
    expanded: boolean,
    refreshing: boolean
}
const RefreshPermissionsButton = styled.div<RefreshPermissionsButtonProps>`
    float: left;
    display: ${props => {
        return props.expanded ? "block" : "none";
    }};
    width: 5rem;
    height: 5rem;
    border-radius: 50%;
    color: ${props => {
        return props.refreshing ? colorError : colorSuccess;
    }};
    .Refreshing {
        animation: ${refreshing} 1s ease-in-out infinite;
    }
    &:hover {
        cursor: pointer;
        background-color: ${() => colorBlueDark};
    }
`

type SidebarToggleButtonProps = {
    flipped: boolean
}
const SidebarToggleButton = styled.div<SidebarToggleButtonProps>`
    float: right;
    display: block;
    width: 5rem;
    height: 5rem;
    transform: ${props => {
        return props.flipped ? "rotateY(180deg)" : "none"
    }};
    transition-duration: 0.5s;
    &:hover {
        cursor: pointer;
        background-color: ${() => colorBlueDark};
    }
`

const TopBar = styled.div`
    display: flex;
    height: 5rem;
    padding-left: 5px;
    background-color: ${() => colorOrange};
    box-shadow: 0 0.1rem 0.2rem #666666;
`

const PersonName = styled.div`
    display: flex;
    width: fit-content;
    height: 100%;
    font-size: 1.8rem;
    align-items: center;
`

const Hello = styled.div`
    font-size: 1.5rem;
`

const PersonNameText = styled.div`
    font-weight: bold;
    text-align: center;
`

const LogoutButton = styled.div`
    position: absolute;
    display: flex;
    right: 1rem;
    width: fit-content;
    height: 5rem;
    align-items: center;
    padding: 0 1rem;
    font-size: 1.5rem;
    &:hover {
        cursor: pointer;
        background-color: ${() => colorOrangeDark};
    }
    &:active {
        background-color: ${() => colorOrangeDarker};
    }
`

type ContentProps = {
    stretched: boolean
}
const Content = styled.div<ContentProps>`
    position: relative;
    display: inline-block;
    width: ${props => {
        return props.stretched ? "calc(100% - 5rem)" : "calc(100% - 30rem)"
    }};
    height: 100%;
    transition-duration: 0.5s;
`

const RouteContent = styled.div`
    position: absolute;
    left: 0;
    top: 5rem;
    right: 0;
    bottom: 0;
    padding: 5px;
`

const sidebarToggleArrowTheme = createTheme({
    components: {
        MuiSvgIcon: {
            styleOverrides: {
                root: {
                    width: "3rem",
                    height: "3rem",
                    margin: "1rem",
                    color: colorOrange
                }
            }
        }
    }
});

const keyTheme = createTheme({
    components: {
        MuiSvgIcon: {
            styleOverrides: {
                root: {
                    position: "absolute",
                    left: "1.5rem",
                    top: "1.5rem",
                    width: "2rem",
                    height: "2rem",
                    transform: "rotateZ(-90deg)"
                }
            }
        }
    }
});

const loopTheme = createTheme({
    components: {
        MuiSvgIcon: {
            styleOverrides: {
                root: {
                    position: "absolute",
                    left: "0",
                    top: "0",
                    width: "5rem",
                    height: "5rem"
                }
            }
        }
    }
});

export default function WindowFrame(props: {children: RouterModel[]}): React.ReactElement {
    const sidebarExpanded = useAppSelector(state => state.sidebar.sidebarExpanded);
    const accessToken = getAccessToken();
    const dispatch = useAppDispatch();
    const [refreshing, setRefreshing] = useState<boolean>(false);

    function refreshPermissions() {
        if (refreshing) {
            return;
        }
        setRefreshing(true);
        refreshPermissionsApi().then(() => {
            setRefreshing(false);
            ToastUtils.showSuccess("Permissions refreshed");
        }).catch(() => {
            setRefreshing(false);
            ToastUtils.showError("Failed to refresh permissions");
        });
    }

    function onSidebarToggle() {
        dispatch(setSidebarExpanded(!sidebarExpanded));
    }

    useEffect(() => {
        metadataApi().then(response => {
            dispatch(setPersonName({
                firstName: response.resultBody.firstName,
                lastName: response.resultBody.lastName
            }));
        }).catch(() => {});
    }, [dispatch]);

    function usePersonName() {
        let personName = useAppSelector(state => state.personName);
        return (personName.firstName ? personName.firstName : "") +
            (personName.lastName ? " " + personName.lastName : "");
    }

    function logout() {
        logoutApi().then(() => {
            removeAccessToken();
            window.location.reload();
        }).catch(() => {});
    }
    let menuTree = useAppSelector(state => state.menu.menuTree);

    return <RootWindow>
        <Sidebar expanded={sidebarExpanded}>
            <SidebarToggleButtonWrapper>
                <RefreshPermissionsButton
                    expanded={sidebarExpanded}
                    refreshing={refreshing}
                    onClick={() => refreshPermissions()}>
                    <ThemeProvider theme={keyTheme}>
                        <Key/>
                    </ThemeProvider>
                    <ThemeProvider theme={loopTheme}>
                        <Refresh className={refreshing ? "Refreshing" : ""}/>
                    </ThemeProvider>
                </RefreshPermissionsButton>
                <SidebarToggleButton flipped={sidebarExpanded} onClick={onSidebarToggle}>
                    <ThemeProvider theme={sidebarToggleArrowTheme}>
                        <ArrowForward/>
                    </ThemeProvider>
                </SidebarToggleButton>
            </SidebarToggleButtonWrapper>
            <SidebarItem items={menuTree} indent={2} parentKey={0}/>
        </Sidebar>
        <Content stretched={!sidebarExpanded}>
            <TopBar>
                <PersonName>
                    <Hello>Hello,&nbsp;</Hello>
                    <PersonNameText>{ usePersonName() }</PersonNameText>
                </PersonName>
                <LogoutButton onClick={logout}>Logout</LogoutButton>
            </TopBar>
            <RouteContent>
                <Routes>
                    { props.children.map((child, index) => createRoute(child, index, accessToken)) }
                </Routes>
            </RouteContent>
        </Content>
    </RootWindow>
}
