import {Navigate, Route, Routes} from "react-router-dom";
import Register from "./register";
import CenterContainer from "../../components/center-container";
import {Paper} from "@mui/material";

const AccountRoutes = () => (
	<Routes>
		{/*<Route path="/login" element={<Login />}/>*/}
		<Route path="/register" element={<Paper><CenterContainer><Register/></CenterContainer></Paper>}/>
		<Route path="*" element={<Navigate to="/register"/>}/>
	</Routes>
);

export default AccountRoutes;
