import {BASE_URL} from "./constants";
import {httpPost} from "./helper-functions";
import {User} from "./types/user";

const ACCOUNT_URL = `${BASE_URL}/Authenticate`

export const register = (user: User) => httpPost(`${ACCOUNT_URL}/register`, user);
