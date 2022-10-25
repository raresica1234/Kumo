import {Navigate, Route, Routes} from "react-router-dom";
import Home from "./home";
import PermissionManager from "./permission-manager";
import {EXPLORER_PAGE, PERMISSIONS_PAGE} from "../../utils/constants";

interface MainRouteProps {
    isAdmin: boolean;
}

const MainRoutes = ({isAdmin}: MainRouteProps) => (<Routes>
        <Route path="" element={<Home/>}/>
        <Route path="/" element={<Home/>}/>
        <Route path={EXPLORER_PAGE} element={<Home/>}/>
        {isAdmin && <Route path={PERMISSIONS_PAGE} element={<PermissionManager/>}/>}
        <Route path="*" element={<Navigate to="/"/>}/>
    </Routes>);

export default MainRoutes;
