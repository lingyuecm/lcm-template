import Cookies from 'js-cookie';

const ACCESS_TOKEN = "accessToken";

export function setAccessToken(accessToken) {
    Cookies.set(ACCESS_TOKEN, accessToken);
}

export function getAccessToken() {
    return Cookies.get(ACCESS_TOKEN);
}