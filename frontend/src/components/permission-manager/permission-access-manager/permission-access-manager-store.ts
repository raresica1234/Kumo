import {ChangeEvent, createContext} from "react";
import PermissionModel from "../../../models/permission-model";
import Role from "../../../accessors/types/role";
import PathPoint from "../../../accessors/types/path-point";
import {getAllRoles} from "../../../accessors/role-accessor";
import {makeAutoObservable, runInAction, toJS} from "mobx";
import {getAllPathPoints} from "../../../accessors/path-point-accessor";
import {
	createPermission,
	deletePermission,
	getAllPermissions,
	updatePermission
} from "../../../accessors/permission-accessor";
import Permission from "../../../accessors/types/permission";
import {SelectChangeEvent} from "@mui/material";
import {GridValueSetterParams} from "@mui/x-data-grid";
import {toastServiceStore} from "../../../infrastructure/toast-service/toast-service-store";

class PermissionAccessManagerStore {
	private permissions: PermissionModel[] = [];
	public roles: Role[] = [];
	public pathPoints: PathPoint[] = [];
	public permissionsToDisplay: PermissionModel[] = [];
	public dirty: boolean = false;

	public roleId: string = "";
	public pathPointId: string = "";

	public read: boolean = false;
	public write: boolean = false;
	public deletable: boolean = false;
	public modifyRoot: boolean = false;

	constructor() {
		makeAutoObservable(this);
	}

	public init = () => {
		this.fetchAll().then();
	}

	public fetchAll = async () => {
		try {
			const roles = await getAllRoles();
			const pathPoints = await getAllPathPoints();
			const permissions: Permission[] = await getAllPermissions();
			const permissionsToDisplay: PermissionModel[] = permissions.map(permission => ({
				...permission,
				roleName: roles.find(role => role.id === permission.roleId)?.name ?? "Not found",
				pathPoint: pathPoints.find(pathPoint => pathPoint.id === permission.pathPointId)?.path ?? "Not found",
				dirty: false,
				deleted: false,
			}))

			runInAction(() => {
				this.dirty = false;
				this.roles = roles;
				this.pathPoints = pathPoints;
				this.permissions = permissionsToDisplay;
				this.permissionsToDisplay = toJS(permissionsToDisplay);
			})
		} catch (e: any) {
			toastServiceStore.showError(e.toString());
		}
	}

	public handleAddPermission = async () => {
		try {
			await createPermission({
				roleId: this.roleId,
				pathPointId: this.pathPointId,
				read: this.read,
				write: this.write,
				delete: this.deletable,
				modifyRoot: this.modifyRoot
			});

			runInAction(() => {
				this.roleId = this.pathPointId = "";
				this.read = this.write = this.deletable = this.modifyRoot = false;
			})

			await this.fetchAll();
			toastServiceStore.showSuccess("Permission successfully added!");
		} catch (e: any) {
			toastServiceStore.showError(e.toString());
		}
	}

	public markPermissionAsDeleted = (roleId: string, pathPointId: string, deleted: boolean) => {
		const index = this.permissions.findIndex(permission => permission.roleId === roleId && permission.pathPointId === pathPointId);
		if (index === -1)
			return;

		runInAction(() => {
			this.permissions[index].deleted = deleted;
			this.permissions[index].dirty = true;
			this.permissionsToDisplay[index].deleted = deleted;
			this.dirty = true;
		})
	}

	public commitChanges = async () => {
		try {
			await Promise.all(this.permissions.filter(permission => permission.deleted).map(permission => deletePermission(permission.roleId, permission.pathPointId)));

			const dirtyPermission = this.permissions.filter(permissions => !permissions.deleted && permissions.dirty)

			await Promise.all(dirtyPermission.map(permission => updatePermission(permission)));

			runInAction(() => {
				dirtyPermission.forEach(dirtyPermission => dirtyPermission.dirty = false);
				this.dirty = false;
				this.permissionsToDisplay = toJS(this.permissions.filter(permission => !permission.deleted));
			})
			toastServiceStore.showSuccess("Changes successfully committed!");
		} catch (e: any) {
			toastServiceStore.showError(e.toString());
		}
	}

	public modifyPermission = (model: PermissionModel) => {
		const index = this.permissions.findIndex(permission => permission.roleId === model.roleId && permission.pathPointId === model.pathPointId);
		if (index === -1) {
			toastServiceStore.showError("Could not find permission!");
			return model;
		}
		model.dirty = true;

		runInAction(() => {
			this.permissions[index] = model;
			this.dirty = true;
		})
		return model;
	}

	public handleRoleChange = (event: SelectChangeEvent) => {
		runInAction(() => {
			this.roleId = event.target.value as string;
		})
	}

	public handlePathPointChange = (event: SelectChangeEvent) => {
		runInAction(() => {
			this.pathPointId = event.target.value as string;
		})
	}

	public handleReadChange = (event: ChangeEvent, checked: boolean) => {
		runInAction(() => {
			this.read = checked;
		})
	}
	public handleWriteChange = (event: ChangeEvent, checked: boolean) => {
		runInAction(() => {
			this.write = checked;
		})
	}
	public handleModifyRootChange = (event: ChangeEvent, checked: boolean) => {
		runInAction(() => {
			this.modifyRoot = checked;
		})
	}
	public handleDeletableChange = (event: ChangeEvent, checked: boolean) => {
		runInAction(() => {
			this.deletable = checked;
		})
	}

	public handleValueSetter = (params: GridValueSetterParams, field: string) => {
		const model = {...params.row, [field]: params.value};
		if (params.row[field] != params.value)
			return this.modifyPermission(model);
		return model;
	}
}

export const permissionAccessManagerStore = new PermissionAccessManagerStore();
export const PermissionAccessManagerContext = createContext(permissionAccessManagerStore);
