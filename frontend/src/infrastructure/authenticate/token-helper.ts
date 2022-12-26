import Token from "../../accessors/types/token";

const JWT_TOKEN_KEY = "jwt_token";
const REFRESH_TOKEN_KEY = "refresh_token"

export const setToken = (token: Token) => {
    localStorage.setItem(JWT_TOKEN_KEY, token.jwtToken);
    localStorage.setItem(REFRESH_TOKEN_KEY, token.refreshToken);
}

export const getJwtToken = () => localStorage.getItem(JWT_TOKEN_KEY) ?? "";
export const getRefreshToken = () => localStorage.getItem(REFRESH_TOKEN_KEY) ?? "";


export const clearTokens = () => {
    localStorage.removeItem(JWT_TOKEN_KEY)
    localStorage.removeItem(REFRESH_TOKEN_KEY)
}
