import {Grid, Paper} from "@mui/material";
import styles from './home.module.scss'
import TopBar from "../../../components/top-bar";
import Explorer from "../explorer";
import {observer} from "mobx-react";

const Home = () => {
	return <>
		<TopBar/>
		<Paper square elevation={0} className={styles.container}
			   component={Grid} container>
		<Explorer/>
		</Paper>
	</>
}

export default observer(Home);
