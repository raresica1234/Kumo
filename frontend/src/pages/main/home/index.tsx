import {Paper} from "@mui/material";
import styles from './home.module.scss'
import TopBar from "../../../components/top-bar";
import Explorer from "../explorer";
import {observer} from "mobx-react";
import {useLocation} from "react-router-dom";

const Home = () => {
	const search = useLocation().search;
	const path = new URLSearchParams(search).get('path');

	return <div className={styles.mainPage}>
		<TopBar/>
		<Paper square elevation={0} className={styles.container}>
			<Paper square elevation={8} className={styles.innerContainer}>
				<Explorer path={path ?? undefined}/>
			</Paper>
		</Paper>
	</div>

}

export default observer(Home);
