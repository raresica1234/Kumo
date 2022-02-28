import React, {useContext, useEffect} from 'react';
import './App.scss';
import {AuthenticateContext} from "./infrastructure/authenticate";
import {BrowserRouter} from "react-router-dom";
import AccountRoutes from "./pages/accounts";
import {createTheme, ThemeProvider} from "@mui/material";
import {observer} from "mobx-react";
import {blueGrey, green, grey, orange} from "@mui/material/colors";

const App = () => {
	const {isUserLogged, init} = useContext(AuthenticateContext);
	const theme = createTheme({
		palette: {
			mode: "dark",
			primary: {
				main: blueGrey[700]
			},
			secondary: {
				main: orange[400]
			}
			// text: {
			// 	primary: "#fff",
			// 	secondary: "#ddd"
			// },
			// background: {
			// 	paper: "#000"
			// }
		},
		typography: {
			allVariants: {
				color: "white"
			}
		}
	})

	useEffect(() => {
		init();
	}, [init]);

	if (isUserLogged === undefined) {
		return null;
	}

	return <ThemeProvider theme={theme}>
		<BrowserRouter>
			{isUserLogged ? (
				<></>
			) : (
				<AccountRoutes/>
			)}
		</BrowserRouter>
	</ThemeProvider>
};

export default observer(App);
