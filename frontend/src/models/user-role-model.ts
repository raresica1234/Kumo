import UserRole from "../accessors/types/user-role";
import EditableModel from "./editable-model";

export default interface UserRoleModel extends UserRole, EditableModel {
	username: string;
	roleName: string;
}
