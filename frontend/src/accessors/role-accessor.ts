import {httpDelete, httpGet, httpPost, httpPut} from "./helper-functions";
import {BASE_URL} from "./constants";
import Role from "./types/role";
import RoleCreate from "./types/role-create";

const ROLE_URL = `${BASE_URL}/Role`

export const getAllRoles = () => httpGet<Role[]>(ROLE_URL)
export const createRole = (role: RoleCreate) => httpPost(ROLE_URL, role);
export const updateRole = (role: Role) => httpPut(ROLE_URL, role);
export const deleteRole = (roleId: string) => httpDelete(ROLE_URL, roleId);
