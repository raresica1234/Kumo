import React, {useContext, useEffect} from 'react';
import {AuthenticateContext} from "./infrastructure/authenticate";
import {BrowserRouter} from "react-router-dom";
import AccountRoutes from "./pages/accounts";
import {observer} from "mobx-react";
import MainRoutes from "./pages/main";
import WrappedToastService from "./infrastructure/toast-service";
import Themer from "./components/themer";
import ServerDown from "./components/server-down";

const App = () => {
    const {isUserLogged, init, isUserAdmin, isServerDown} = useContext(AuthenticateContext);

    useEffect(() => {
        init();
    }, [init, isUserLogged === undefined ? null : isUserLogged]);

    console.log(`${isUserLogged} - ${isUserAdmin}`)

    if (isUserLogged === undefined || isUserAdmin === undefined) return null;

    return <Themer>
        {isServerDown ?
            <ServerDown/>
            :
            <BrowserRouter>
                {isUserLogged ?
                    <MainRoutes isAdmin={isUserAdmin}/>
                    :
                    <AccountRoutes/>
                }
            </BrowserRouter>
        }
        <WrappedToastService/>
    </Themer>
};

export default observer(App);
