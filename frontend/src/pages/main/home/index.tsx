import {Grid, Paper} from "@mui/material";
import styles from './home.module.scss'
import TopBar from "../../../components/top-bar";

const Home = () => {
	return <>
		<TopBar/>
		<Paper square elevation={0} className={styles.container}>
			<Grid container>
				a
			</Grid>
		</Paper>
	</>
}

export default Home;
