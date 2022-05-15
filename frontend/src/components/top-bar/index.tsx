import {AppBar, Button, Toolbar, Typography} from "@mui/material";
import CloudIcon from '@mui/icons-material/Cloud';
import LogoutIcon from '@mui/icons-material/Logout';
import styles from './top-bar.module.scss'
import {useNavigate} from "react-router-dom";
import {AuthenticateContext, authenticateStore} from "../../infrastructure/authenticate";
import {useContext} from "react";

const TopBar = () => {
	const {reset} = useContext(AuthenticateContext);

	return <AppBar position="sticky">
		<Toolbar>
			<CloudIcon/>
			<Typography className={styles.typography}>
				Kumo
			</Typography>
			<Button color={"error"} endIcon={<LogoutIcon/>} onClick={() => reset()}>
				Logout
			</Button>
		</Toolbar>
	</AppBar>
}

export default TopBar;
