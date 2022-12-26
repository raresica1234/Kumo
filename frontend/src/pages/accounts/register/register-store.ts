import {RegisterUserForm} from "../../../accessors/types/register-user";
import {makeAutoObservable} from "mobx";
import {register} from "../../../accessors/account-accessor";
import {createContext} from "react";

export class RegisterStore {
	public user: RegisterUserForm = {
		username: "",
		email: "",
		password: "",
		confirmPassword: ""
	};

	constructor() {
		makeAutoObservable(this)
	}

	public setUsername = (username: string) => this.user.username = username;

	public setEmail = (email: string) => this.user.email = email;

	public setPassword = (password: string) => this.user.password = password;

	public setConfirmPassword = (confirmPassword: string) => this.user.confirmPassword = confirmPassword;

	public register = async (): Promise<string> => {
		if (this.user.password !== this.user.confirmPassword) {
			return "Passwords don't match"
		}

		try {
			await register({email: this.user.email, username: this.user.username, password: this.user.password});
			return ""
		} catch (error) {
			return error as string
		}
	}
}

export const registerStore = new RegisterStore()
export const RegisterContext = createContext(registerStore)
