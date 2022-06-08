import {observer} from "mobx-react";
import MainPageContainer from "../../../components/main-page/main-page-container";
import {Accordion, AccordionDetails, AccordionSummary, Grid, Typography} from "@mui/material";
import PathPointManager from "../../../components/permission-manager/path-point-manager";
import {useState} from "react";
import RoleManager from "../../../components/permission-manager/role-manager";
import PermissionAccessManager from "../../../components/permission-manager/permission-access-manager";
import UserRoleManager from "../../../components/permission-manager/user-role-manager";

const PermissionManager = () => {
	const [expanded, setExpanded] = useState('');

	const handleChange = (newExpanded: boolean, panel: string) => {
		setExpanded(newExpanded ? panel : "");
	};

	return <MainPageContainer>
		<Grid container direction="column" padding={2} spacing={1}>
			<Grid item>
				<Accordion expanded={expanded === 'panel1'}
						   onChange={(event, expanded) => handleChange(expanded, 'panel1')}>
					<AccordionSummary aria-controls="panel3d-content" id="panel3d-header">
						<Typography>Manage Path Points</Typography>
					</AccordionSummary>
					<AccordionDetails>
						<PathPointManager opened={expanded === 'panel1'}/>
					</AccordionDetails>
				</Accordion>
			</Grid>
			<Grid item>
				<Accordion expanded={expanded === 'panel2'}
						   onChange={(event, expanded) => handleChange(expanded, 'panel2')}>
					<AccordionSummary aria-controls="panel3d-content" id="panel3d-header">
						<Typography>Manage Roles</Typography>
					</AccordionSummary>
					<AccordionDetails>
						<RoleManager opened={expanded === 'panel2'}/>
					</AccordionDetails>
				</Accordion>
			</Grid>
			<Grid item>
				<Accordion expanded={expanded === 'panel3'}
						   onChange={(event, expanded) => handleChange(expanded, 'panel3')}>
					<AccordionSummary aria-controls="panel3d-content" id="panel3d-header">
						<Typography>Manage Permissions</Typography>
					</AccordionSummary>
					<AccordionDetails>
						<PermissionAccessManager opened={expanded === 'panel3'}/>
					</AccordionDetails>
				</Accordion>
			</Grid>
			<Grid item>
				<Accordion expanded={expanded === 'panel4'}
						   onChange={(event, expanded) => handleChange(expanded, 'panel4')}>
					<AccordionSummary aria-controls="panel3d-content" id="panel3d-header">
						<Typography>Manage Users</Typography>
					</AccordionSummary>
					<AccordionDetails>
						<UserRoleManager opened={expanded === 'panel4'}/>
					</AccordionDetails>
				</Accordion>
			</Grid>
		</Grid>
	</MainPageContainer>
}

export default observer(PermissionManager);
