import React, {useEffect, useState} from 'react'
import { refreshCaptchaApi, loginApi } from '../../api/userApi'
import { setAccessToken } from "../../utils/cacheManager";
import {createTheme, Grid, ThemeProvider} from "@mui/material";
import styled from "styled-components";
import {colorBlue, colorBlueDark, colorBlueDarker, colorGreyD2} from "../../utils/constant";
import {LoginRequest} from "../../model/request";

const RootContainer = styled.div`
    margin-top: 30rem;
`

const Welcome = styled.div`
    margin-bottom: 3rem;
    color: ${() => {
        return colorBlue;
    }};
    font-size: 5rem;
    font-weight: bolder;
    text-align: center;
`

const InputFrame = styled.input`
    width: 100%;
    height: 100%;
    font-size: 1.5rem;
    vertical-align: top;
    box-sizing: border-box;
    border: ${() => "1px solid " + colorGreyD2};
    border-radius: 1rem;
    padding: 0 1rem;
`

const CaptchaImage = styled.img`
    height: 100%;
    margin-left: 1rem;
    border-radius: 0.5rem;
    &:hover {
        cursor: pointer;
    }
`

const LoginButton = styled.div`
    display: flex;
    width: 100%;
    height: 100%;
    background-color: ${() => colorBlue};
    box-shadow: 0 0 0.1rem #666666;
    border: 1px solid #D2D2D2;
    border-radius: 1rem;
    align-items: center;
    &:hover {
        cursor: pointer;
        background-color: ${() => colorBlueDark}
    }
    &:active {
        background-color: ${() => colorBlueDarker}
    }
`

const LoginButtonText = styled.div`
    width: 100%;
    font-size: 1.5rem;
    text-align: center;
`

const Divider = styled.div`
    height: 1rem;
`

const loginTheme = createTheme({
    components: {
        MuiGrid: {
            styleOverrides: {
                container: {
                    height: "5rem"
                }
            }
        }
    }
});

export default function Login() {
    const [captchaBase64, setCaptchaBase64] = useState<string>('');
    const [token, setToken] = useState<string>('');
    const [phoneNo, setPhoneNo] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [captcha, setCaptcha] = useState<string>('');

    function onPhoneNoChange(e: React.ChangeEvent<HTMLInputElement>) {
        setPhoneNo(e.target.value);
    }

    function onPasswordChange(e: React.ChangeEvent<HTMLInputElement>) {
        setPassword(e.target.value);
    }

    function onCaptchaChange(e: React.ChangeEvent<HTMLInputElement>) {
        setCaptcha(e.target.value);
    }

    function refreshCaptcha() {
        refreshCaptchaApi({
            captchaWidth: 100,
            captchaHeight: 40
        }).then(response => {
            setCaptchaBase64('data:image/png;base64,' + response.resultBody.captchaImage);
            setToken(response.resultBody.token);
        }).catch(() => {});
    }

    function login() {
        const requestBody: LoginRequest = {
            phoneNo,
            password,
            captcha,
            token
        }

        loginApi(requestBody).then(response => {
            setAccessToken(response.resultBody.token);
            window.location.href = "/";
        }).catch(() => {});
    }

    useEffect(() => {
        refreshCaptcha();
    }, []);

    return <RootContainer>
        <Welcome>Welcome to the Simple RBAC System</Welcome>
        <ThemeProvider theme={loginTheme}>
            <Grid container spacing={0}>
                <Grid item xs={4}></Grid>
                <Grid item xs={4}>
                    <InputFrame placeholder={"Phone Number"} onChange={onPhoneNoChange}/>
                </Grid>
                <Grid item xs={4}></Grid>
            </Grid>
            <Divider></Divider>
            <Grid container spacing={0}>
                <Grid item xs={4}></Grid>
                <Grid item xs={4}>
                    <InputFrame placeholder={"Password"} type={"password"} onChange={onPasswordChange}/>
                </Grid>
                <Grid item xs={4}></Grid>
            </Grid>
            <Divider></Divider>
            <Grid container spacing={0}>
                <Grid item xs={4}></Grid>
                <Grid item xs={4}>
                    <InputFrame placeholder={"Captcha"} onChange={onCaptchaChange}/>
                </Grid>
                <Grid item xs={2}>
                    <CaptchaImage src={captchaBase64} alt={"Captcha"}
                         onClick={() => refreshCaptcha()}/>
                </Grid>
            </Grid>
            <Divider></Divider>
            <Grid container spacing={0}>
                <Grid item xs={4}></Grid>
                <Grid item xs={4}>
                    <LoginButton onClick={() => login()}>
                        <LoginButtonText>Login</LoginButtonText>
                    </LoginButton>
                </Grid>
                <Grid item xs={4}></Grid>
            </Grid>
        </ThemeProvider>
    </RootContainer>
}
