import {createContext} from "react";
import {makeAutoObservable, runInAction, toJS} from "mobx";
import RoleModel from "../../../models/role-model";
import {createRole, deleteRole, getAllRoles, updateRole} from "../../../accessors/role-accessor";
import {debounce} from "@mui/material";
import {toastServiceStore} from "../../../infrastructure/toast-service/toast-service-store";

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
		try {
			const roles = await getAllRoles();
			runInAction(() => {
				this.dirty = false;
				this.roles = roles.map(role => ({...role, dirty: false, deleted: false}));
				this.rolesToDisplay = toJS(this.roles);
			})
		} catch (e: any) {
			toastServiceStore.showError(e.toString());
		}
	}

	public modifyRole = (model: RoleModel) => {
		const index = this.roles.findIndex(role => role.id === model.id);
		if (index === -1) {
			toastServiceStore.showError("Could not find role!");
			return model;
		}
		model.dirty = true;

		runInAction(() => {
			this.roles[index] = model;
			this.dirty = true;
		})
		return model;
	}

	public commitChanges = async () => {
		try {
			await Promise.all(this.roles.filter(role => role.deleted).map(role => deleteRole(role.id)));

			const dirtyRoles = this.roles.filter(role => !role.deleted && role.dirty)

			await Promise.all(dirtyRoles.map(role => updateRole(role)));
			runInAction(() => {
				dirtyRoles.forEach(dirtyPathPoint => dirtyPathPoint.dirty = false);
				this.dirty = false;
				this.rolesToDisplay = toJS(this.roles.filter(role => !role.deleted));
			})
			toastServiceStore.showSuccess("Changes successfully committed!");
		} catch (e: any) {
			toastServiceStore.showError(e.toString());
		}
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
		try {
			await createRole({name: this.roleName});
			await this.fetchRoles();
			runInAction(() => {
				this.roleName = "";
			})
			toastServiceStore.showSuccess("Role successfully added!");
		} catch (e: any) {
			toastServiceStore.showError(e.toString());
		}
	}
}


export const roleManagerStore = new RoleManagerStore();
export const RoleManagerContext = createContext(roleManagerStore);
