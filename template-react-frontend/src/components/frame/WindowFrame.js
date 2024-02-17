import {ArrowForward} from "@mui/icons-material";
import {Routes} from "react-router-dom";
import {createRoute} from "../../rounter";
import {getAccessToken, removeAccessToken} from "../../utils/cacheManager";
import {useDispatch, useSelector} from "react-redux";
import {setSidebarExpanded} from "../../store/sidebarSlice";
import {useEffect} from "react";
import {metadataApi} from "../../api/userApi";
import {setPersonName} from "../../store/personNameSlice";
import SidebarItem from "./SidebarItem";
import styled from "styled-components";
import {colorBlueDark, colorOrange, colorOrangeDark, colorOrangeDarker} from "../../utils/constant";
import {createTheme, ThemeProvider} from "@mui/material";

const RootWindow = styled.div`
    display: flex;
    width: 100%;
    height: 100vh;
    word-wrap: break-word;
`

const Sidebar = styled.div`
    display: inline-block;
    width: ${props => {
        return props.expanded ? "30rem" : "5rem"
    }};
    height: 100%;
    background-color: #5982B6;
    box-shadow: 0.1rem 0 0.2rem #666666;
    transition-duration: 0.5s;
    z-index: 100;
`

const SidebarToggleButtonWrapper = styled.div`
    width: 100%;
    height: 5rem;
`

const SidebarToggleButton = styled.div`
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

const Content = styled.div`
    position: relative;
    display: inline-block;
    //width: calc(100% - 30rem);
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

export default function WindowFrame(props) {
    const sidebarExpanded = useSelector(state => state.sidebar.sidebarExpanded);
    const accessToken = getAccessToken();
    const dispatch = useDispatch();

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
        let personName = useSelector(state => state.personName);
        return (personName.firstName ? personName.firstName : "") +
            (personName.lastName ? " " + personName.lastName : "");
    }

    function logout() {
        removeAccessToken();
        window.location.reload();
    }
    let menuTree = useSelector(state => state.menu.menuTree);

    return <RootWindow>
        <Sidebar expanded={sidebarExpanded}>
            <SidebarToggleButtonWrapper>
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
