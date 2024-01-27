import React from 'react'
import './Login.css'
import { refreshCaptchaApi, loginApi } from '../../api/userApi'
import {Grid} from "@mui/material";

class Login extends React.Component {
    constructor(props) {
        super(props)
        this.onPhoneNoChange = this.onPhoneNoChange.bind(this)
        this.onPasswordChange = this.onPasswordChange.bind(this)
        this.onCaptchaChange = this.onCaptchaChange.bind(this)
        this.refreshCaptcha = this.refreshCaptcha.bind(this)
        this.login = this.login.bind(this)
    }
    state = {
        captchaBase64: '',
        token: '',
        phoneNo: '',
        password: '',
        captcha: ''
    }
    componentDidMount() {
        this.refreshCaptcha()
    }
    render() {
        return (
            <div className={"Root-container"}>
                <Grid container spacing={0} className={"Input-wrapper"}>
                    <Grid item xs={4}></Grid>
                    <Grid item xs={4}>
                        <input className={"Input-frame"} placeholder={"Phone Number"} onChange={this.onPhoneNoChange}/>
                    </Grid>
                    <Grid item xs={4}></Grid>
                </Grid>
                <div className={"Divider"}></div>
                <Grid container spacing={0} className={"Input-wrapper"}>
                    <Grid item xs={4}></Grid>
                    <Grid item xs={4}>
                        <input className={"Input-frame"} placeholder={"Password"} type={"password"}
                               onChange={this.onPasswordChange}/>
                    </Grid>
                    <Grid item xs={4}></Grid>
                </Grid>
                <div className={"Divider"}></div>
                <Grid container spacing={0} className={"Input-wrapper"}>
                    <Grid item xs={4}></Grid>
                    <Grid item xs={4}>
                        <input className={"Input-frame"} placeholder={"Captcha"} onChange={this.onCaptchaChange}/>
                    </Grid>
                    <Grid item xs={2}>
                        <img className={"Captcha"} src={this.state.captchaBase64} alt={"Captcha"}
                             onClick={this.refreshCaptcha}/>
                    </Grid>
                </Grid>
                <div className={"Divider"}></div>
                <Grid container spacing={0} className={"Input-wrapper"}>
                    <Grid item xs={4}></Grid>
                    <Grid item xs={4}>
                        <div className={"Login-button"} onClick={this.login}>
                            <div className={"Login-button-text"}>Login</div>
                        </div>
                    </Grid>
                    <Grid item xs={4}></Grid>
                </Grid>
            </div>
        )
    }

    onPhoneNoChange(e) {
        this.setState({
            phoneNo: e.target.value
        })
    }

    onPasswordChange(e) {
        this.setState({
            password: e.target.value
        })
    }

    onCaptchaChange(e) {
        this.setState({
            captcha: e.target.value
        })
    }

    refreshCaptcha() {
        refreshCaptchaApi({
            captchaWidth: 100,
            captchaHeight: 40
        }).then(response => {
            this.setState({
                captchaBase64: 'data:image/png;base64,' + response.resultBody.captchaImage,
                token: response.resultBody.token
            })
        }).catch(error => {
            alert('Error: ' + JSON.stringify(error))
        })
    }

    login() {
        const requestBody = {}
        requestBody.phoneNo = this.state.phoneNo
        requestBody.password = this.state.password
        requestBody.captcha = this.state.captcha
        requestBody.token = this.state.token

        loginApi(requestBody).then(response => {
            console.log(response.resultBody.token);
            window.location.replace("/")
        }).catch(error => {
            alert('Error' + JSON.stringify(error))
        })
    }
}

export default Login;
