import {observer} from "mobx-react";
import {PropsWithChildren, useContext, useEffect} from "react";
import {PermissionAccessManagerContext} from "./permission-access-manager-store";
import {
	Box,
	Button,
	ButtonGroup,
	Checkbox,
	FormControl,
	FormControlLabel,
	Grid,
	IconButton,
	InputLabel,
	MenuItem,
	Select,
	Typography
} from "@mui/material";
import {DataGrid, GridValueSetterParams} from "@mui/x-data-grid";
import {toJS} from "mobx";
import DeleteIcon from "@mui/icons-material/Delete";
import CancelIcon from "@mui/icons-material/Cancel";

interface Props {
	opened: boolean;
}

const PermissionAccessManager = ({opened}: PropsWithChildren<Props>) => {
	const {
		permissionsToDisplay,
		roles,
		pathPoints,
		dirty,

		roleId,
		pathPointId,
		read,
		write,
		deletable,
		modifyRoot,

		init,
		fetchAll,
		handleAddPermission,
		markPermissionAsDeleted,
		commitChanges,

		handleRoleChange,
		handlePathPointChange,
		handleReadChange,
		handleWriteChange,
		handleDeletableChange,
		handleModifyRootChange,

		handleValueSetter
	} = useContext(PermissionAccessManagerContext);

	useEffect(() => {
		if (opened)
			init();
	}, [init, opened]);

	if (roles.length === 0 || pathPoints.length === 0)
		return <>Add a role and a path point to continue</>

	return <Grid container direction={"column"} spacing={4}>
		<Grid item>
			<DataGrid style={{height: "350px"}}
					  experimentalFeatures={{newEditingApi: true}}
					  rows={toJS(permissionsToDisplay)}
					  getRowId={(row) => row.roleId + " " + row.pathPointId}
					  columns={[
						  {
							  field: 'roleName',
							  headerName: "Role",
							  width: 300,
							  editable: false,
						  },
						  {
							  field: 'pathPoint',
							  headerName: "Path",
							  width: 400,
							  // type: "boolean",
							  editable: false,
						  },
						  {
							  field: "modifyRoot",
							  headerName: "ModifyRoot",
							  width: 150,
							  type: "boolean",
							  editable: true,
							  valueSetter: (params: GridValueSetterParams) => handleValueSetter(params, "modifyRoot")
						  },
						  {
							  field: "read",
							  headerName: "Read",
							  width: 150,
							  type: "boolean",
							  editable: true,
							  valueSetter: (params: GridValueSetterParams) => handleValueSetter(params, "read")
						  },
						  {
							  field: "write",
							  headerName: "Write",
							  width: 150,
							  type: "boolean",
							  editable: true,
							  valueSetter: (params: GridValueSetterParams) => handleValueSetter(params, "write")
						  },
						  {
							  field: "delete",
							  headerName: "Delete",
							  width: 150,
							  type: "boolean",
							  editable: true,
							  valueSetter: (params: GridValueSetterParams) => handleValueSetter(params, "delete")
						  },
						  {
							  field: 'remove',
							  headerName: "",
							  hideSortIcons: true,
							  disableColumnMenu: true,
							  renderCell: (params) => {
								  const handleClick = (e: any, deleted: boolean) => {
									  e.stopPropagation();
									  markPermissionAsDeleted(params.row.roleId, params.row.pathPointId, deleted);
								  }

								  if (!params.row.deleted)
									  return <IconButton onClick={(e: any) => handleClick(e, true)}>
										  <DeleteIcon/>
									  </IconButton>
								  else
									  return <IconButton onClick={(e: any) => handleClick(e, false)}>
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
				<Typography>Add Permission</Typography>
				<FormControl fullWidth>
					<InputLabel id="permission-access-manager-role-select">Role</InputLabel>
					<Select labelId="permission-access-manager-role-select"
							value={roleId}
							label={"Role"}
							onChange={handleRoleChange}
					>
						{roles.map(role => {
							return <MenuItem key={role.id} value={role.id}>{role.name}</MenuItem>
						})}
					</Select>
				</FormControl>
				<FormControl fullWidth>
					<InputLabel id="permission-access-manager-path-point-select">Path point</InputLabel>
					<Select labelId="permission-access-manager-path-point-select"
							value={pathPointId}
							label={"Path point"}
							onChange={handlePathPointChange}
					>
						{pathPoints.map(pathPoint => {
							return <MenuItem key={pathPoint.id} value={pathPoint.id}>{pathPoint.path}</MenuItem>
						})}
					</Select>
				</FormControl>
				<FormControlLabel control={<Checkbox checked={read} onChange={handleReadChange}/>}
								  label={"Read"}/> <br/>
				<FormControlLabel control={<Checkbox checked={write} onChange={handleWriteChange}/>}
								  label={"Write"}/> <br/>
				<FormControlLabel control={<Checkbox checked={deletable} onChange={handleDeletableChange}/>}
								  label={"Delete"}/> <br/>
				<FormControlLabel control={<Checkbox checked={modifyRoot} onChange={handleModifyRootChange}/>}
								  label={"Modify Root"}/> <br/>
				<Button variant={"contained"} color={"secondary"} onClick={handleAddPermission}>
					Add permission
				</Button>
			</Box>

		</Grid>
	</Grid>
}

export default observer(PermissionAccessManager);


