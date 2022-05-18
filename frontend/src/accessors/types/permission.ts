export default interface Permission {
	roleId: string;
	pathPointId: string;
	read: boolean;
	write: boolean;
	delete: boolean;
	modifyRoot: boolean;
}
