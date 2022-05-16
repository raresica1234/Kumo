import {Navigate, Route, Routes} from "react-router-dom";
import Home from "./home";
import PermissionManager from "./permission-manager";

const MainRoutes = () => (
	<Routes>
		<Route path="" element={<Home/>}/>
		<Route path="/" element={<Home/>}/>
		<Route path="/explore" element={<Home/>}/>
		<Route path="/permissions" element={<PermissionManager/>}/>
		<Route path="*" element={<Navigate to="/"/>}/>
	</Routes>
);

export default MainRoutes;
