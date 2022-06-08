import {createContext} from "react";
import Role from "../../../accessors/types/role";
import {makeAutoObservable, runInAction, toJS} from "mobx";
import {createUserRole, deleteUserRole, getAllUserRoles, getAllUsers} from "../../../accessors/user-role-accessor";
import {getAllRoles} from "../../../accessors/role-accessor";
import UserRoleModel from "../../../models/user-role-model";
import {User} from "../../../accessors/types/user";
import {SelectChangeEvent} from "@mui/material";
import {toastServiceStore} from "../../../infrastructure/toast-service/toast-service-store";

class UserRoleManagerStore {
	private userRoles: UserRoleModel[] = [];
	public users: User[] = [];
	public roles: Role[] = [];
	public userRolesToDisplay: UserRoleModel[] = [];
	public dirty: boolean = false;

	public userId: string = "";
	public roleId: string = "";

	constructor() {
		makeAutoObservable(this);
	}

	public init = () => {
		this.fetchAll();
	}

	public fetchAll = async () => {
		try {
			const users = await getAllUsers();
			const roles = await getAllRoles();
			const userRoles = await getAllUserRoles();

			const userRolesToDisplay: UserRoleModel[] = userRoles.map(userRole => ({
				...userRole,
				username: users.find(user => user.id === userRole.userId)?.username ?? "Not found",
				roleName: roles.find(role => role.id === userRole.roleId)?.name ?? "Not found",
				dirty: false,
				deleted: false
			}));

			runInAction(() => {
				this.dirty = false;
				this.roles = roles;
				this.users = users;
				this.userRoles = userRolesToDisplay;
				this.userRolesToDisplay = toJS(userRolesToDisplay);
			})
		} catch (e: any) {
			toastServiceStore.showError(e.toString());
		}
	}

	public commitChanges = async () => {
		try {
			await Promise.all(this.userRoles.filter(userRole => userRole.deleted).map(userRole => deleteUserRole(userRole.userId, userRole.roleId)));

			runInAction(() => {
				this.userRolesToDisplay = toJS(this.userRoles.filter(userRole => !userRole.deleted));
			})
			toastServiceStore.showSuccess("Changes successfully committed!");
		} catch (e: any) {
			toastServiceStore.showError(e.toString());
		}
	}

	public markUserRoleAsDeleted = (userId: string, roleId: string, deleted: boolean) => {
		const index = this.userRoles.findIndex(userRole => userRole.userId === userId && userRole.roleId == roleId);
		if (index === -1)
			return;

		runInAction(() => {
			this.userRoles[index].deleted = deleted;
			this.userRoles[index].dirty = true;
			this.userRolesToDisplay[index].deleted = deleted;
			this.dirty = true;
		})
	}

	public handleAddUserRole = async () => {
		try {
			await createUserRole({userId: this.userId, roleId: this.roleId});

			runInAction(() => {
				this.userId = "";
				this.roleId = "";
			})

			await this.fetchAll();
			toastServiceStore.showSuccess("UserRole successfully added");
		} catch (e: any) {
			toastServiceStore.showError(e.toString());
		}
	}

	public handleUserChange = (event: SelectChangeEvent) => {
		runInAction(() => {
			this.userId = event.target.value;
		})
	}

	public handleRoleChange = (event: SelectChangeEvent) => {
		runInAction(() => {
			this.roleId = event.target.value;
		})
	}

}

export const userRoleManagerStore = new UserRoleManagerStore();
export const UserRoleManagerContext = createContext(userRoleManagerStore);
