import axios from "axios";
import {getJwtToken, getRefreshToken} from "../../infrastructure/authenticate/token-helper";
import {BASE_URL} from "../constants";
import Token from "../types/token";
import {authenticateStore} from "../../infrastructure/authenticate";

export const http = axios.create({
    baseURL: BASE_URL,
    timeout: 5000,
    headers: {
        "Content-Type": "application/json",
    },
})

var refreshFlow = false;

http.interceptors.request.use(config => {
    if (config.headers && !refreshFlow)
        config.headers.Authorization = `Bearer ${getJwtToken()}`

    return config;
}, error => {
    return Promise.reject(error);
});



http.interceptors.response.use(value => {
    return value;
}, async error => {
    const {config, message, response} = error;

    if (refreshFlow) {
        authenticateStore.reset();

        return Promise.reject(error);
    }

    if (response.data && response.data.httpStatus !== undefined) {
        if (response.data.message.includes("authentication")) {
            refreshFlow = true;
            const token = await http.post<Token>("/authenticate/refresh-token", undefined, {
                headers: {
                    'Refresh-Token': `Bearer ${getRefreshToken()}`,
                },
            });
            console.log(token);
        }
    }
    authenticateStore.reset();

    return Promise.reject(error);
});