const KEY_TOKEN = "token";

export const setToken = (token: string) => localStorage.setItem(KEY_TOKEN, token);

export const getToken = () => localStorage.getItem(KEY_TOKEN) ?? "";

export const clearToken = () => localStorage.removeItem(KEY_TOKEN)
