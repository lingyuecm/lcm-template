import axios from "axios";
import {getAccessToken} from "./cacheManager";

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
        return Promise.reject(response.data)
    }
}, error => {
    return Promise.reject(error)
});

export default service;
