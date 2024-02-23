import {Snackbar} from "@mui/material";
import ToastUtils from "../utils/ToastUtils";
import {useEffect, useState} from "react";
import styled from "styled-components";
import {
    colorError,
    colorErrorLight, colorGrey64, colorGreyA6,
    colorSuccess,
    colorSuccessLight,
    colorWarning,
    colorWarningLight
} from "../utils/constant";

const TYPE_SUCCESS = "success";
const TYPE_ERROR = "error";
const TYPE_WARNING = "warning";
const TYPE_INFO = "info";

type HttpMethodType = {
    type: string
}

const SnackbarMessage = styled.div<HttpMethodType>`
    display: flex;
    width: 40vw;
    color: ${props => {
        if (TYPE_SUCCESS === props["type"]) {
            return colorSuccess;
        }
        else if (TYPE_ERROR === props["type"]) {
            return colorError;
        }
        else if (TYPE_WARNING === props["type"]) {
            return colorWarning;
        }
        else {
            return colorGrey64;
        }
    }};
    font-size: 1.8rem;
    font-weight: bold;
    border: ${props => {
        let borderColor;
        if (TYPE_SUCCESS === props["type"]) {
            borderColor = colorSuccess;
        }
        else if (TYPE_ERROR === props["type"]) {
            borderColor = colorError;
        }
        else if (TYPE_WARNING === props["type"]) {
            borderColor = colorWarning;
        }
        else {
            borderColor = colorGrey64;
        }
        return "0.1rem solid " + borderColor;
    }};
    border-radius: 0.8rem;
    padding: 1rem;
    background-color: ${props => {
        if (TYPE_SUCCESS === props["type"]) {
            return colorSuccessLight;
        }
        else if (TYPE_ERROR === props["type"]) {
            return colorErrorLight;
        }
        else if (TYPE_WARNING === props["type"]) {
            return colorWarningLight;
        }
        else {
            return colorGreyA6;
        }
    }};
    box-shadow: 0 0.1rem 0.2rem #666666;
    align-items: center;
`

export default function Toast() {
    const [toastShown, setToastShown] = useState(false);
    const [message, setMessage] = useState("");
    const [messageType, setMessageType] = useState("");

    function onToastHidden() {
        setToastShown(false);
    }

    useEffect(() => {
        ToastUtils.showSuccess = message => {
            setMessage(message);
            setMessageType(TYPE_SUCCESS);
            setToastShown(true);
        }

        ToastUtils.showError = message => {
            setMessage(message);
            setMessageType(TYPE_ERROR);
            setToastShown(true);
        }

        ToastUtils.showWarning = message => {
            setMessage(message);
            setMessageType(TYPE_WARNING);
            setToastShown(true);
        }

        ToastUtils.showInfo = message => {
            setMessage(message);
            setMessageType(TYPE_INFO);
            setToastShown(true);
        }
    }, []);

    return <Snackbar
        open={toastShown}
        anchorOrigin={{vertical: "top", horizontal: "center"}}
        autoHideDuration={3000}
        onClose={onToastHidden}
        message={message}>
        <SnackbarMessage type={messageType}>
            <div style={{width: "100%", textAlign: "center"}}>{message}</div>
        </SnackbarMessage>
    </Snackbar>
}
