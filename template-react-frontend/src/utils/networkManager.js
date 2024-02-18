import axios from "axios";
import {getAccessToken, removeAccessToken} from "./cacheManager";
import ToastUtils from "./ToastUtils";

const STATUS_OK = 0;
const STATUS_INVALID_TOKEN = -5;

const service = axios.create({
    baseURL: '/api'
});

service.interceptors.request.use(config => {
    config.headers['Access-Token'] = getAccessToken();
    return config;
});

service.interceptors.response.use(response => {
    if (response.data.resultCode === STATUS_OK) {
        return response.data;
    }
    else if (response.data.resultCode === STATUS_INVALID_TOKEN) {
        ToastUtils.showError(response.data["resultMessage"]);
        removeAccessToken();
        setTimeout(() => {
            window.location.reload();
        }, 3000);
    }
    else {
        ToastUtils.showError(response.data["resultMessage"]);
        return Promise.reject(response.data)
    }
}, error => {
    ToastUtils.showError(error.message);
    return Promise.reject(error)
});

export default service;
