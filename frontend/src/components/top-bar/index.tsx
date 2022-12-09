import {AppBar, Box, IconButton, Toolbar, Typography} from "@mui/material";
import CloudIcon from '@mui/icons-material/Cloud';
import React, {useContext, useEffect, useState} from "react";
import {observer} from "mobx-react";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import DarkModeIcon from '@mui/icons-material/DarkMode';
import WbSunnyIcon from '@mui/icons-material/WbSunny';
import {ThemeContext} from "../../infrastructure/theme";
import AccountMenu from "../account-menu";
import NavigationDrawer from "../navigation-drawer";

const TopBar = () => {
    const {init, isDarkMode, toggleDarkMode} = useContext(ThemeContext);

    useEffect(init, [init]);

    const [anchorEl, setAnchorEl] = useState<HTMLElement | null>(null);

    const handleAccountClick = (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        setAnchorEl(event.currentTarget);
    }

    const onClose = () => setAnchorEl(null);

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
                {/*<Button color="primary" onClick={() => handleHomeClick()} sx={{my: 2, display: 'block'}}>*/}
                {/*    Home*/}
                {/*</Button>*/}
                {/*{isUserAdmin ? (*/}
                {/*    <Button onClick={() => handlePermissionManagerClick()} sx={{my: 2, display: 'block'}}>*/}
                {/*        Permission Manager*/}
                {/*    </Button>*/}
                {/*) : (<></>)}*/}
            </Box>

            <Box sx={{flexGrow: 0}}>
                <IconButton onClick={toggleDarkMode}>
                    {isDarkMode() ?
                        <WbSunnyIcon fontSize="large"/>
                        :
                        <DarkModeIcon fontSize="large"/>
                    }
                </IconButton>
                <IconButton onClick={handleAccountClick}>
                    <AccountCircleIcon fontSize="large"/>
                </IconButton>
            </Box>
            <div>
                <AccountMenu anchorElement={anchorEl} onClose={onClose}/>
            </div>
        </Toolbar>
    </AppBar>
}

export default observer(TopBar);
