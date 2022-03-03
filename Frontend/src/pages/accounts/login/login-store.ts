import {makeAutoObservable} from "mobx";
import {createContext} from "react";
import {User} from "../../../accessors/types/user";
import {login} from "../../../accessors/account-accessor";
import {authenticateStore} from "../../../infrastructure/authenticate";

export class LoginStore {
	public user: User = {
		email: "",
		password: "",
	};

	constructor() {
		makeAutoObservable(this)
	}

	public setEmail = (email: string) => this.user.email = email;

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
