import axios from "axios";

const service = axios.create({
    baseURL: '/api'
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
})

export default service;
