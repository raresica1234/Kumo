import {AppBar, Toolbar, Typography} from "@mui/material";
import CloudIcon from '@mui/icons-material/Cloud';

const TopBar = () => {

	return <AppBar position="static">
		<Toolbar>
			<CloudIcon/>
			<Typography variant="h6">
				Kumo
			</Typography>

		</Toolbar>
	</AppBar>
}

export default TopBar;
