import Permission from "../accessors/types/permission";
import EditableModel from "./editable-model";

export default interface PermissionModel extends Permission, EditableModel {
	roleName: string;
	pathPoint: string;
}
