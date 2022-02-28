import {Navigate, Route, Routes} from "react-router-dom";
import Register from "./register";
import CenterContainer from "../../components/center-container";

const AccountRoutes = () => (
	<Routes>
		{/*<Route path="/login" element={<Login />}/>*/}
		<Route path="/register" element={<CenterContainer><Register/></CenterContainer>}/>
		<Route path="*" element={<Navigate to="/register"/>}/>
	</Routes>
);

export default AccountRoutes;
