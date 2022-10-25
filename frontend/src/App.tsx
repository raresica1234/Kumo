import React, {useContext, useEffect} from 'react';
import {AuthenticateContext} from "./infrastructure/authenticate";
import {BrowserRouter} from "react-router-dom";
import AccountRoutes from "./pages/accounts";
import {Grid, Paper, ThemeProvider} from "@mui/material";
import {observer} from "mobx-react";
import CenterContainer from "./components/center-container";
import MainRoutes from "./pages/main";
import WrappedToastService from "./infrastructure/toast-service";
import {ThemeContext} from "./infrastructure/theme";

const App = () => {
    const {isUserLogged, init: initAuthentication, isUserAdmin} = useContext(AuthenticateContext);
    const {theme, init: initTheming} = useContext(ThemeContext);

    useEffect(() => {
        initAuthentication();
        initTheming();
    }, [initTheming, initAuthentication, isUserLogged]);

    console.log(`${theme}, ... ${isUserLogged}`)

    if (isUserLogged === undefined || theme === undefined || isUserAdmin === undefined)
        return null;

    return <ThemeProvider theme={theme}>
        <BrowserRouter>
            {isUserLogged ? (
                <MainRoutes isAdmin={isUserAdmin}/>
            ) : (
                <Paper square elevation={0}>
                    <Grid container>
                        <Grid item xs={0} sm={2} lg={4}/>
                        <Grid item xs={12} sm={8} lg={4}>
                            <CenterContainer>
                                <AccountRoutes/>
                            </CenterContainer>
                        </Grid>
                        <Grid item xs={0} sm={2} lg={4}/>
                    </Grid>
                </Paper>
            )}
        </BrowserRouter>
        <WrappedToastService/>
    </ThemeProvider>
};

export default observer(App);
