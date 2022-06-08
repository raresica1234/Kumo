import {observer} from "mobx-react";
import {PropsWithChildren, useContext, useEffect} from "react";
import {UserRoleManagerContext} from "./user-role-manager-store";
import {
	Box,
	Button,
	ButtonGroup,
	FormControl,
	Grid,
	IconButton,
	InputLabel,
	MenuItem,
	Select,
	Typography
} from "@mui/material";
import {DataGrid} from "@mui/x-data-grid";
import {toJS} from "mobx";
import DeleteIcon from "@mui/icons-material/Delete";
import CancelIcon from "@mui/icons-material/Cancel";

interface Props {
	opened: boolean
}

const UserRoleManager = ({opened}: PropsWithChildren<Props>) => {
	const {
		users,
		roles,
		userRolesToDisplay,
		dirty,

		userId,
		roleId,

		init,
		fetchAll,
		commitChanges,
		markUserRoleAsDeleted,

		handleUserChange,
		handleRoleChange,
		handleAddUserRole,
	} = useContext(UserRoleManagerContext);


	useEffect(() => {
		if (opened)
			init();
	}, [init, opened]);

	if (roles.length === 0)
		return <>Add a role to continue</>

	return <Grid container direction={"column"} spacing={4}>
		<Grid item>
			<DataGrid style={{height: "350px"}}
					  rows={toJS(userRolesToDisplay)}
					  getRowId={(row) => row.userId + " " + row.roleId}
					  columns={[
						  {
							  field: 'username',
							  headerName: "User",
							  width: 300,
						  },
						  {
							  field: 'roleName',
							  headerName: "Role",
							  width: 150,
						  },
						  {
							  field: 'delete',
							  headerName: "",
							  hideSortIcons: true,
							  disableColumnMenu: true,
							  renderCell: (params) => {
								  const handleClick = (e: any, deleted: boolean) => {
									  e.stopPropagation();
									  markUserRoleAsDeleted(params.row.userId, params.row.roleId, deleted);
								  }

								  if (!params.row.deleted)
									  return <IconButton onClick={(e) => handleClick(e, true)}>
										  <DeleteIcon/>
									  </IconButton>
								  else
									  return <IconButton onClick={(e) => handleClick(e, false)}>
										  <CancelIcon/>
									  </IconButton>
							  }
						  }
					  ]}
					  pageSize={5}
					  rowsPerPageOptions={[5]}
			/>
			<ButtonGroup>
				<Button disabled={!dirty} color={"secondary"} onClick={commitChanges}>
					Save
				</Button>
				<Button disabled={!dirty} onClick={fetchAll}>
					Cancel
				</Button>
			</ButtonGroup>
		</Grid>
		<Grid item>
			<Box sx={{minWidth: 120, maxWidth: 300}}>
				<Typography>Add User Role</Typography>
				<FormControl fullWidth>
					<InputLabel id="user-role-manager-user-select">User</InputLabel>
					<Select labelId="user-role-manager-user-select"
							value={userId}
							label={"User"}
							onChange={handleUserChange}
					>
						{users.map(user => {
							return <MenuItem key={user.id} value={user.id}>{user.username}</MenuItem>
						})}
					</Select>
				</FormControl>
				<FormControl fullWidth>
					<InputLabel id="user-role-manager-role-select">Role</InputLabel>
					<Select labelId="user-role-manager-role-select"
							value={roleId}
							label={"Role"}
							onChange={handleRoleChange}
					>
						{roles.map(role => {
							return <MenuItem key={role.id} value={role.id}>{role.name}</MenuItem>
						})}
					</Select>
				</FormControl>
				<Button variant={"contained"} color={"secondary"} onClick={handleAddUserRole}>
					Add permission
				</Button>
			</Box>
		</Grid>
	</Grid>
}

export default observer(UserRoleManager);
