import {observer} from "mobx-react";
import {useContext} from "react";
import {AuthenticateContext} from "../../infrastructure/authenticate";
import {Menu, MenuItem} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {PERMISSIONS_PAGE} from "../../utils/constants";

interface AccountMenuProps {
    anchorElement: HTMLElement | null;
    onClose: () => void;
}

const AccountMenu = ({anchorElement, onClose}: AccountMenuProps) => {
    const {reset, isUserAdmin} = useContext(AuthenticateContext);

    const navigate = useNavigate();

    const goToAdmin = () => {
        navigate(PERMISSIONS_PAGE)
    }

    return <Menu
        anchorEl={anchorElement}
        open={Boolean(anchorElement)}
        onClose={onClose}
    >
        <MenuItem>Settings</MenuItem>
        {isUserAdmin && <MenuItem onClick={goToAdmin}>Admin</MenuItem>}
        <MenuItem onClick={reset}>Logout</MenuItem>
    </Menu>
}

export default observer(AccountMenu)