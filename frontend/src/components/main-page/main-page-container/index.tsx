import React, {PropsWithChildren} from "react";
import styles from "./main-page-container.module.scss";
import TopBar from "../../top-bar";
import {Paper} from "@mui/material";
import NavigationDrawer from "../../navigation-drawer";

const MainPageContainer = ({children}: PropsWithChildren<any>) => {
	return <>
		<div className={styles.mainPage}>
			<TopBar/>
			{/*<NavigationDrawer/>*/}
			<Paper square elevation={0} className={styles.container}>
				<Paper square elevation={8} className={styles.innerContainer}>
					{children}
				</Paper>
			</Paper>
		</div>
	</>
}

export default MainPageContainer;
