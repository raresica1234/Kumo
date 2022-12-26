
export interface RegisterUserRequest {
	username: string,
	email: string,
	password: string
}

export interface RegisterUserForm extends RegisterUserRequest{
	username: string,
	email: string,
	password: string,
	confirmPassword: string
}
