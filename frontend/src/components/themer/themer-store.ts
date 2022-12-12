import {makeAutoObservable} from "mobx";
import {createContext} from "react";
import {createTheme, Theme} from "@mui/material";
import {lightBlue, orange} from "@mui/material/colors";

export class ThemerStore {
    public theme?: Theme = undefined;

    constructor() {
        makeAutoObservable(this)
    }

    public init = () =>  {
        this.createDarkTheme();
    }

    public isDarkMode = () => {
        return this.theme?.palette.mode === "dark"
    }

    public toggleDarkMode = () => {
        if (!this.theme)
            return;

        if (this.isDarkMode())
            this.createLightTheme();
        else
            this.createDarkTheme();
    }

    private createLightTheme() {
        this.theme = createTheme({
            palette: {
                mode: "light",
                primary: lightBlue,
                secondary: orange,
            },

        })
    }

    private createDarkTheme() {
        this.theme = createTheme({
            palette: {
                mode: "dark",
                primary: lightBlue,
                secondary: orange,
            },
            components: {
                MuiDrawer: {
                    styleOverrides: {
                        paper: {
                            marginTop: "3em"
                        }
                    }
                }
            }
        })
    }
}

export const themeStore = new ThemerStore();
export const ThemeContext = createContext(themeStore);
