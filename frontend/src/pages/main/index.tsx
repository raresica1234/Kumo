import {Navigate, Route, Routes} from "react-router-dom";
import Home from "./home";

const MainRoutes = () => (
	<Routes>
		<Route path="" element={<Home/>}/>
		<Route path="/" element={<Home/>}/>
		<Route path="*" element={<Navigate to="/"/>}/>
	</Routes>
);

export default MainRoutes;
