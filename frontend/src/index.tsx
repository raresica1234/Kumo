import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {CssBaseline} from "@mui/material";
import {createRoot} from "react-dom/client";

// ReactDOM.render(
// 	<React.StrictMode>
// 		<CssBaseline>
// 			<App/>
// 		</CssBaseline>
// 	</React.StrictMode>,
// 	document.getElementById('root')
// );

const container = document.getElementById("root");
const root = createRoot(container!)
root.render(
	<React.StrictMode>
		<CssBaseline>
			<App/>
		</CssBaseline>
	</React.StrictMode>
)

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
