import React, {useContext, useEffect} from 'react';
import {AuthenticateContext} from "./infrastructure/authenticate";
import {BrowserRouter} from "react-router-dom";
import AccountRoutes from "./pages/accounts";
import {createTheme, Grid, Paper, ThemeProvider} from "@mui/material";
import {observer} from "mobx-react";
import {deepOrange, lightBlue, orange} from "@mui/material/colors";
import CenterContainer from "./components/center-container";
import MainRoutes from "./pages/main";

const App = () => {
	const {isUserLogged, init} = useContext(AuthenticateContext);
	const theme = createTheme({
		palette: {
			mode: "dark",
			primary: deepOrange,
			secondary: lightBlue
		}
	})

	useEffect(() => {
		init();
	}, [init, isUserLogged]);

	if (isUserLogged === undefined ) {
		return null;
	}

	return <ThemeProvider theme={theme}>
		<BrowserRouter>
			{isUserLogged ? (
				<MainRoutes />
			) : (
				<Paper square elevation={0}>
					<Grid container>
						<Grid item xs={0} sm={2} lg={4}/>
						<Grid item xs={12} sm={8} lg={4}>
								<CenterContainer>
										<AccountRoutes/>
								</CenterContainer>
						</Grid>
						<Grid item xs={0} sm={2} lg={4}/>
					</Grid>
				</Paper>
			)}
		</BrowserRouter>
	</ThemeProvider>
};

export default observer(App);
