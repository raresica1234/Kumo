import ClientLocation from "./client-location";

export interface LoginUser {
	username: string,
	password: string
	clientLocation: ClientLocation;
}
