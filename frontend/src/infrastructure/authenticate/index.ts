import {createContext} from "react";
import {makeAutoObservable, runInAction} from "mobx";
import {clearTokens, getJwtToken, setToken} from "./token-helper";
import {isAdministrator} from "../../accessors/account-accessor";
import Token from "../../accessors/types/token";

class AuthenticateStore {
    public isUserLogged?: boolean = undefined;
    public isUserAdmin?: boolean = undefined;
    public isServerDown?: boolean = undefined;

    constructor() {
        makeAutoObservable(this);
    }

    public init = () => {
        this.isUserLogged = getJwtToken() !== "";
        this.fetchAdminStatus();
    }

    public setToken = (token?: Token) => {
        if (token !== undefined) {
            setToken(token);
            this.isUserLogged = true;
        } else {
            clearTokens()
            this.isUserLogged = false;
        }

        this.fetchAdminStatus()
    }

    public reset = () => {
        this.isUserLogged = false;
        clearTokens();
        this.fetchAdminStatus()
    }

    private fetchAdminStatus = () => {
        if (!this.isUserLogged) {
            this.isServerDown = false;
            this.isUserAdmin = false;
            return;
        }

        isAdministrator().then(admin => {
            // runInAction(() => this.isUserAdmin = admin);
        }).catch(() => {
            runInAction(() => {
                this.isServerDown = true;
                this.isUserAdmin = false;
            });
        });
    }
}


export const authenticateStore = new AuthenticateStore();
export const AuthenticateContext = createContext(authenticateStore);
