import './WindowFrame.css';
import {ArrowForward} from "@mui/icons-material";
import {Routes} from "react-router-dom";
import {createRoute} from "../../rounter";
import {getAccessToken, removeAccessToken} from "../../utils/cacheManager";
import {useDispatch, useSelector} from "react-redux";
import {setSidebarExpanded} from "../../store/sidebarSlice";
import {useEffect} from "react";
import {metadataApi} from "../../api/userApi";
import {setPersonName} from "../../store/personNameSlice";
import SidebarItem from "../SidebarItem";

export default function WindowFrame(props) {
    const sidebarExpanded = useSelector(state => state.sidebar.sidebarExpanded);
    const accessToken = getAccessToken();
    const styles = {
        sidebarToggleArrow: {
            width: '3rem',
            height: '3rem',
            margin: '1rem',
            color: '#E7A158'
        }
    }
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
        });
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

    return (
        <div className={"Root-window"}>
            <div className={sidebarExpanded ? "Sidebar" : "Sidebar Sidebar-folded"}>
                <div className={"Sidebar-toggle-button-wrapper"}>
                    <div
                        className={sidebarExpanded ? "Sidebar-toggle-button Sidebar-toggle-button-flipped" : "Sidebar-toggle-button"}
                        onClick={onSidebarToggle}>
                        <ArrowForward style={styles.sidebarToggleArrow}/>
                    </div>
                </div>
                <SidebarItem items={menuTree} indent={2} parentKey={0}/>
            </div>
            <div className={sidebarExpanded ? "Content" : "Content Content-wide"}>
                <div className={"Top-bar"}>
                    <div className={"Person-name"}>
                    <div className={"Hello"}>Hello,&nbsp;</div>
                        <div className={"Person-name-text"}>
                            { usePersonName() }
                        </div>
                    </div>
                    <div className={"Logout-button"} onClick={logout}>Logout</div>
                </div>
                <div style={{paddingLeft: "5px"}}>
                    <Routes>
                        { props.children.map((child, index) => createRoute(child, index, accessToken)) }
                    </Routes>
                </div>
            </div>
        </div>
    )
}
