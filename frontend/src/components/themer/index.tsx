import {PropsWithChildren, useContext, useEffect} from "react";
import {ThemeContext} from "./themer-store";
import {ThemeProvider} from "@mui/material";
import {observer} from "mobx-react";

const Themer = ({children}: PropsWithChildren<{}>) => {
    const {theme, init} = useContext(ThemeContext);

    useEffect(() => {
        init();
    }, [init])

    if (theme == undefined)
        return null;

    return <ThemeProvider theme={theme}>
        {children}
    </ThemeProvider>
}

export default observer(Themer);