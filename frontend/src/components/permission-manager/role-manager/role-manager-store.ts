import {createContext} from "react";
import {makeAutoObservable, runInAction, toJS} from "mobx";
import RoleModel from "../../../models/role-model";
import {createRole, deleteRole, getAllRoles, updateRole} from "../../../accessors/role-accessor";
import {debounce} from "@mui/material";
import {deletePathPoint, updatePathPoint} from "../../../accessors/path-point-accessor";
import PathPointModel from "../../../models/path-point-model";

class RoleManagerStore {
	private roles: RoleModel[] = [];
	public rolesToDisplay: RoleModel[] = [];
	public dirty: boolean = false;
	public roleName: string = "";

	constructor() {
		makeAutoObservable(this);
	}

	public init = () => {
		this.fetchRoles();
	}

	public setRoleName = debounce((roleName) => {
		console.log(roleName);
		this.roleName = roleName;
	}, 400);


	public fetchRoles = async () => {
		const roles = await getAllRoles();
		runInAction(() => {
			this.dirty = false;
			this.roles = roles.map(role => ({...role, dirty: false, deleted: false}));
			this.rolesToDisplay = toJS(this.roles);
		})
	}

	public modifyRole = (model: RoleModel) => {
		const index = this.roles.findIndex(role => role.id === model.id);
		if (index === -1)
			// TODO: Error should be displayed
			return model;
		model.dirty = true;

		runInAction(() => {
			this.roles[index] = model;
			this.dirty = true;
		})
		return model;
	}

	public commitChanges = async () => {
		await Promise.all(this.roles.filter(role => role.deleted).map(role => deleteRole(role.id)));

		const dirtyRoles = this.roles.filter(role => !role.deleted && role.dirty)

		await Promise.all(dirtyRoles.map(role => updateRole(role)));
		runInAction(() => {
			dirtyRoles.forEach(dirtyPathPoint => dirtyPathPoint.dirty = false);
			this.dirty = false;
			this.rolesToDisplay = toJS(this.roles.filter(role => !role.deleted));
		})
	}

	public markRoleAsDeleted = (id: string, deleted: boolean) => {
		const index = this.roles.findIndex(role => role.id === id);
		if (index === -1)
			return;

		runInAction(() => {
			this.roles[index].deleted = deleted;
			this.roles[index].dirty = true;
			this.rolesToDisplay[index].deleted = deleted;
			this.dirty = true;
		})
	}

	public addRole = async () => {
		await createRole({name: this.roleName});
		await this.fetchRoles();
		runInAction(() => {
			this.roleName = "";
		})
	}
}


export const roleManagerStore = new RoleManagerStore();
export const RoleManagerContext = createContext(roleManagerStore);
