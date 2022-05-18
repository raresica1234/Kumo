import {httpDeleteTwoId, httpGet, httpPost} from "./helper-functions";
import {BASE_URL} from "./constants";
import UserRole from "./types/user-role";
import {User} from "./types/user";


const USER_URL = `${BASE_URL}/User`

export const getAllUserRoles = () => httpGet<UserRole[]>(`${USER_URL}/userRoles`)
export const getAllUsers = () => httpGet<User[]>(USER_URL)
export const createUserRole = (userRole: UserRole) => httpPost(USER_URL, userRole);
export const deleteUserRole = (userId: string, roleId: string) => httpDeleteTwoId(USER_URL, userId, roleId);
