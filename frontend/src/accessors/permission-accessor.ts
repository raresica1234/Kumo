import {BASE_URL} from "./constants";
import {httpDeleteTwoId, httpGet, httpPost, httpPut} from "./helper-functions";
import Permission from "./types/permission";

const PERMISSION_URL = `${BASE_URL}/Permission`

export const getAllPermissions = () => httpGet<Permission[]>(PERMISSION_URL)
export const createPermission = (permission: Permission) => httpPost(PERMISSION_URL, permission);
export const updatePermission = (permission: Permission) => httpPut(PERMISSION_URL, permission);
export const deletePermission = (roleId: string, pathPointId: string) => httpDeleteTwoId(PERMISSION_URL, roleId, pathPointId);
