import {User} from "./user";

export interface RegisterUser extends User {
	confirmPassword: string
}
