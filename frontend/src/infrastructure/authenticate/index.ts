import {createContext} from "react";
import {makeAutoObservable} from "mobx";
import {clearToken, getToken, setToken} from "./token-helper";

class AuthenticateStore {
	public isUserLogged?: boolean = undefined;

	constructor() {
		makeAutoObservable(this);
	}

	public init = () => {
		this.isUserLogged = getToken() !== "";
	}

	public setToken = (token?: string) => {
		if (token !== undefined) {
			setToken(token);
			this.isUserLogged = true;
		} else {
			clearToken()
			this.isUserLogged = false;
		}
	}

	public reset = () => {
		this.isUserLogged = false;
		clearToken();
	}
}


export const authenticateStore = new AuthenticateStore();
export const AuthenticateContext = createContext(authenticateStore);
