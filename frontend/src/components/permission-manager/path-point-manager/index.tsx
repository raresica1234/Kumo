import {PropsWithChildren, useContext, useEffect} from "react";
import {PathPointManagerContext} from "./path-point-manager-store";
import {DataGrid, GridRenderCellParams, GridValueSetterParams} from "@mui/x-data-grid";
import {
	Button,
	ButtonGroup,
	Checkbox,
	debounce,
	FormControlLabel,
	Grid,
	IconButton,
	TextField,
	Typography
} from "@mui/material";
import {toJS} from "mobx";
import {observer} from "mobx-react";
import DeleteIcon from '@mui/icons-material/Delete';
import CancelIcon from '@mui/icons-material/Cancel';

interface Props {
	opened: boolean;
}

const PathPointManager = ({opened} : PropsWithChildren<Props>) => {
	const {
		pathPointsToDisplay,
		dirty,
		isRoot,
		pathPoint,
		init,
		setIsRoot,
		setPathPoint,
		fetchPathPoints,
		modifyPathPoint,
		markPathPointAsDeleted,
		commitChanges,
		addPathPoint
	} = useContext(PathPointManagerContext);

	useEffect(() => {
		if(opened)
			init()
	}, [init, opened]);

	const handleValueSetter = (params: GridValueSetterParams, field: string) => {
		const model = {...params.row, [field]: params.value};
		if (params.row[field] != params.value)
			return modifyPathPoint(model)
		return model;
	}

	const handlePathChange = (pathPoint: string) => {
		setPathPoint(pathPoint)
	}

	const columns = [
		{
			field: 'path',
			headerName: "Path",
			width: 300,
			editable: true,
			valueSetter: (params: GridValueSetterParams) => handleValueSetter(params, "path")
		},
		{
			field: 'isRoot',
			headerName: "Is Root Path?",
			width: 150,
			type: "boolean",
			editable: true,
			valueSetter: (params: GridValueSetterParams) => handleValueSetter(params, "isRoot")
		},
		{
			field: 'delete',
			headerName: "",
			hideSortIcons: true,
			disableColumnMenu: true,
			renderCell: (params: GridRenderCellParams) => {
				const handleClick = (e: any, deleted: boolean) => {
					e.stopPropagation();
					markPathPointAsDeleted(params.row.id, deleted);
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
	]

	return <Grid container direction={"column"} spacing={4}>
		<Grid item>
			<DataGrid style={{height: "350px"}}
					  experimentalFeatures={{newEditingApi: true}}
					  rows={toJS(pathPointsToDisplay)}
					  columns={columns}
					  pageSize={5}
					  rowsPerPageOptions={[5]}
			/>
			<ButtonGroup>
				<Button disabled={!dirty} color={"secondary"} onClick={commitChanges}>
					Save
				</Button>
				<Button disabled={!dirty} onClick={fetchPathPoints}>
					Cancel
				</Button>
			</ButtonGroup>
		</Grid>
		<Grid item>
			<Typography>Add Path Point</Typography>
			<TextField fullWidth
					   label="Path"
					   variant="standard"
					   type="text"
					   onChange={(e) => handlePathChange(e.target.value)}/>
			<FormControlLabel control={<Checkbox checked={isRoot} onChange={((e, checked) => setIsRoot(checked))}/>}
							  label={"Is root?"}/> <br/>
			<Button variant={"contained"} color={"secondary"} onClick={addPathPoint}>
				Add path point
			</Button>
		</Grid>
	</Grid>
}

export default observer(PathPointManager);
