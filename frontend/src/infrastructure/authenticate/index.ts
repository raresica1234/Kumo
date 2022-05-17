import {createContext} from "react";
import {makeAutoObservable, runInAction} from "mobx";
import {clearToken, getToken, setToken} from "./token-helper";
import {isAdministrator} from "../../accessors/account-accessor";

class AuthenticateStore {
	public isUserLogged?: boolean = undefined;
	public isUserAdmin?: boolean = undefined;

	constructor() {
		makeAutoObservable(this);
	}

	public init = () => {
		this.isUserLogged = getToken() !== "";
		this.fetchAdminStatus();
	}

	public setToken = (token?: string) => {
		if (token !== undefined) {
			setToken(token);
			this.isUserLogged = true;
		} else {
			clearToken()
			this.isUserLogged = false;
		}

		this.fetchAdminStatus()
	}

	public reset = () => {
		this.isUserLogged = false;
		clearToken();
		this.fetchAdminStatus()
	}

	private fetchAdminStatus = () => {
		if (!this.isUserLogged)
			return;

		isAdministrator().then(admin => {
			runInAction(() => this.isUserAdmin = admin);
		});
	}
}


export const authenticateStore = new AuthenticateStore();
export const AuthenticateContext = createContext(authenticateStore);
