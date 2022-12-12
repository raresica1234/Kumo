import CenterContainer from "../containers/center-container";
import {Paper, Typography} from "@mui/material";
import React from "react";
import DnsIcon from '@mui/icons-material/Dns';
const ServerDown = () => {
    return <Paper square elevation={0}>
        <CenterContainer>
            <div style={{display: "flex", flexDirection: "row"}}>
                <DnsIcon fontSize={"large"}/>
                <Typography variant="h1">Server down</Typography>
            </div>
        </CenterContainer>
    </Paper>
}

export default ServerDown;