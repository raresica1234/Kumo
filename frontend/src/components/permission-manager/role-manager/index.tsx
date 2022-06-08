import {Button, ButtonGroup, Grid, IconButton, TextField, Typography} from "@mui/material";
import {DataGrid, GridValueSetterParams} from "@mui/x-data-grid";
import {toJS} from "mobx";
import DeleteIcon from "@mui/icons-material/Delete";
import CancelIcon from "@mui/icons-material/Cancel";
import {PropsWithChildren, useContext, useEffect} from "react";
import {RoleManagerContext} from "./role-manager-store";
import {observer} from "mobx-react";

interface Props {
	opened: boolean;
}


const RoleManager = ({opened}: PropsWithChildren<Props>) => {
	const {
		rolesToDisplay,
		dirty,
		init,
		fetchRoles,
		commitChanges,
		setRoleName,
		addRole,
		markRoleAsDeleted,
		modifyRole
	} = useContext(RoleManagerContext);

	useEffect(() => {
		if (opened)
			init();
	}, [init, opened]);

	const handleValueSetter = (params: GridValueSetterParams, field: string) => {
		const model = {...params.row, [field]: params.value};
		if (params.row[field] != params.value)
			return modifyRole(model)
		return model;
	}

	const handleRoleNameChange = (roleName: string) => {
		setRoleName(roleName);
	}

	return <Grid container direction={"column"} spacing={4}>
		<Grid item>
			<DataGrid style={{height: "350px"}}
					  experimentalFeatures={{newEditingApi: true}}
					  rows={toJS(rolesToDisplay)}
					  columns={[
						  {
							  field: 'name',
							  headerName: "Name",
							  width: 300,
							  editable: true,
							  valueSetter: (params: GridValueSetterParams) => handleValueSetter(params, "name")
						  },
						  {
							  field: 'delete',
							  headerName: "",
							  hideSortIcons: true,
							  disableColumnMenu: true,
							  renderCell: (params) => {
								  const handleClick = (e: any, deleted: boolean) => {
									  e.stopPropagation();
									  markRoleAsDeleted(params.row.id, deleted);
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
				<Button disabled={!dirty} onClick={fetchRoles}>
					Cancel
				</Button>
			</ButtonGroup>
		</Grid>
		<Grid item>
			<Typography>Add Role</Typography>
			<TextField fullWidth
					   label="Name"
					   variant="standard"
					   type="text"
					   onChange={(e) => handleRoleNameChange(e.target.value)}/>
			<Button variant={"contained"} color={"secondary"} onClick={addRole}>
				Add role
			</Button>
		</Grid>
	</Grid>
}

export default observer(RoleManager);
