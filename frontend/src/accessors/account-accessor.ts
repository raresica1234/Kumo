import {BASE_URL} from "./constants";
import {httpGet, httpPost} from "./helper-functions";
import {LoginUser} from "./types/login-user";
import Token from "./types/token";
import {RegisterUserRequest} from "./types/register-user";

const ACCOUNT_URL = `${BASE_URL}/authenticate`

export const register = (user: RegisterUserRequest) => httpPost(`${ACCOUNT_URL}/register`, user);

export const login = async (user: LoginUser) => {
	const token = await httpPost<Token>(`${ACCOUNT_URL}/login`, user);

	return token;
}

export const isAdministrator = () => httpGet<boolean>(`${ACCOUNT_URL}/isAdministrator`);
