import React, {PropsWithChildren} from "react";
import {Grid, Paper} from "@mui/material";
import CenterContainer from "../center-container";

const AuthContainer = ({children}: PropsWithChildren<{}>) => {
    return <Paper square elevation={0}>
        <Grid container>
            <Grid item xs={0} sm={2} lg={4}/>
            <Grid item xs={12} sm={8} lg={4}>
                <CenterContainer>
                    {children}
                </CenterContainer>
            </Grid>
            <Grid item xs={0} sm={2} lg={4}/>
        </Grid>
    </Paper>
}

export default AuthContainer;