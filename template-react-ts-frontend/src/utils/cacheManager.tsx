import Cookies from "js-cookie";

const ACCESS_TOKEN = "accessToken";

export function setAccessToken(accessToken: string): void {
    Cookies.set(ACCESS_TOKEN, accessToken);
}

export function getAccessToken(): string {
    return Cookies.get(ACCESS_TOKEN) as string;
}

export function removeAccessToken(): void {
    Cookies.remove(ACCESS_TOKEN);
}