import axios from "axios";
import {getAccessToken} from "./cacheManager";
import ToastUtils from "./ToastUtils";

const service = axios.create({
    baseURL: '/api'
});

service.interceptors.request.use(config => {
    config.headers['Access-Token'] = getAccessToken();
    return config;
});

service.interceptors.response.use(response => {
    if (response.data.resultCode === 0) {
        return response.data;
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
