import {AppBar, Box, Button, Toolbar, Typography} from "@mui/material";
import CloudIcon from '@mui/icons-material/Cloud';
import LogoutIcon from '@mui/icons-material/Logout';
import {AuthenticateContext} from "../../infrastructure/authenticate";
import {useContext} from "react";
import {observer} from "mobx-react";
import {useNavigate} from "react-router-dom";

const TopBar = () => {
    const {reset, isUserAdmin} = useContext(AuthenticateContext);

    const navigate = useNavigate();

    const handlePermissionManagerClick = () => {
        navigate("/permissions");
    }
    const handleHomeClick = () => {
        navigate("/");
    }

    return <AppBar position="sticky">
        <Toolbar>
            <Box sx={{flexGrow: 0, display: 'flex', alignItems: 'center'}}>
                <CloudIcon sx={{display: 'block', mr: 1}}/>
                <Typography
                    noWrap
                    sx={{
                        margin: 'auto',
                        mr: 2,
                        display: 'block',
                        color: 'inherit',
                        // fontSize: '0.875rem',
                    }}>
                    KUMO
                </Typography>
            </Box>

            <Box sx={{flexGrow: 1, display: 'flex'}}>
                <Button color="primary" onClick={() => handleHomeClick()} sx={{my: 2, display: 'block'}}>
                    Home
                </Button>
                {isUserAdmin ? (
                    <Button onClick={() => handlePermissionManagerClick()} sx={{my: 2, display: 'block'}}>
                        Permission Manager
                    </Button>
                ) : (<></>)}
            </Box>

            <Box sx={{flexGrow: 0}}>
                <Button color={"error"} endIcon={<LogoutIcon/>} onClick={() => reset()}>
                    Logout
                </Button>
            </Box>
        </Toolbar>
    </AppBar>
}

export default observer(TopBar);
