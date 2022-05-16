import {AppBar, Button, Toolbar, Typography} from "@mui/material";
import CloudIcon from '@mui/icons-material/Cloud';
import LogoutIcon from '@mui/icons-material/Logout';
import styles from './top-bar.module.scss'
import {AuthenticateContext} from "../../infrastructure/authenticate";
import {useContext} from "react";
import {observer} from "mobx-react";
import {useNavigate} from "react-router-dom";

const TopBar = () => {
	const {reset, isUserAdmin} = useContext(AuthenticateContext);

	const navigate = useNavigate();

	const handlePermissionManagerClick = () => {
		navigate("/permissions");
	}

	return <AppBar position="sticky">
		<Toolbar>
			<CloudIcon/>
			<Typography className={styles.typography}>
				Kumo
			</Typography>

			{isUserAdmin ? (
				<Button onClick={() => handlePermissionManagerClick()}>
					Permission Manager
				</Button>
			) :(<></>)}

			<Button color={"error"} endIcon={<LogoutIcon/>} onClick={() => reset()}>
				Logout
			</Button>

		</Toolbar>
	</AppBar>
}

export default observer(TopBar);
