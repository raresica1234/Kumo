import {makeAutoObservable} from "mobx";
import {createContext} from "react";
import {LoginUser} from "../../../accessors/types/login-user";
import {login} from "../../../accessors/account-accessor";
import {authenticateStore} from "../../../infrastructure/authenticate";

export class LoginStore {
	public user: LoginUser = {
		username: "",
		password: "",
		clientLocation: {
			country: "",
			ipAddress: "",
			locationType: "WEB",
		}
	};

	constructor() {
		makeAutoObservable(this)
	}

	public setUsername = (username: string) => this.user.username = username;

	public setPassword = (password: string) => this.user.password = password;

	public login = async (): Promise<string> => {
		try {
			const token = await login(this.user);
			authenticateStore.setToken(token);

			return ""
		} catch (error) {
			return error as string;
		}
	}

}

export const loginStore = new LoginStore()
export const LoginContext = createContext(loginStore)
