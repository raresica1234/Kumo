import {BASE_URL} from "./constants";
import {httpGet, httpPost} from "./helper-functions";
import {LoginUser} from "./types/login-user";
import Token from "./types/token";
import {RegisterUserRequest} from "./types/register-user";
import {http} from "./services/axios-instances";

const ACCOUNT_URL = `${BASE_URL}/authenticate`

export const register = (user: RegisterUserRequest) => httpPost(`${ACCOUNT_URL}/register`, user);

export const login = async (user: LoginUser) => {
	const token = await http.post<Token>(`/login`, user);

	console.log(token);

	return token;
}

export const isAdministrator = () => http.post<boolean>("/isAdministrator");
