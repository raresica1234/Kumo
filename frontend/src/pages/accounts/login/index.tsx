import {Button, Grid, Link, Paper, TextField, Typography} from "@mui/material";
import styles from "./../accounts.module.scss"
import {useContext, useState} from "react";
import {LoginContext} from "./login-store";
import {useNavigate} from "react-router-dom";
import AuthContainer from "../../../components/containers/auth-container";

const Login = () => {
    const navigate = useNavigate();

    const {
        setUsername,
        setPassword,
        login
    } = useContext(LoginContext)

    const initErrorStates = () => {
        return {
            emailError: <></>,
            passwordError: <></>
        };
    }

    const [errorState, setErrorState] = useState(initErrorStates());

    const handleLogin = async () => {
        const result = await login();

        console.log(result)

        if (result == "") {
            navigate("/");
        } else {
            let errorStates = initErrorStates();

            const errors = result.split("\n");
            errors.forEach(error => {
                if (error.toLowerCase().includes("email") || error.toLowerCase().includes("username")) {
                    errorStates.emailError = <>{errorStates.emailError}{error}<br/></>;
                }
                if (error.toLowerCase().includes("password")) {
                    errorStates.passwordError = <>{errorStates.passwordError}{error}<br/></>;
                }
            });

            setErrorState(errorStates);
        }
    }

    const handleEnter = async (key: string) => {
        if (key === "Enter")
            await handleLogin();
    }

    return <AuthContainer>
        <Paper elevation={6}
               component={Grid} container
               spacing={1}
               className={styles.container}
               direction={"column"}
               alignItems={"center"}
               maxWidth={"sm"}
               justifyContent={"center"}>
            <Grid item>
                <Typography variant="h3">Login</Typography>
            </Grid>
            <Grid item className={styles.inputContainer}>
                <TextField fullWidth label="Email" variant="standard" type="email"
                           error={errorState.emailError.props.children !== undefined}
                           helperText={errorState.emailError}
                           onChange={(e) => setUsername(e.target.value)}
                           onKeyDown={(e) => handleEnter(e.key)}/>
            </Grid>
            <Grid item xs className={styles.inputContainer}>
                <TextField fullWidth
                           type="password"
                           label="Password"
                           variant="standard"
                           error={errorState.passwordError.props.children !== undefined}
                           helperText={errorState.passwordError}
                           onChange={(e) => setPassword(e.target.value)}
                           onKeyDown={(e) => handleEnter(e.key)}/>
            </Grid>

            <Grid item xs className={styles.inputContainer}>
                <div className={styles.submit}>
                    <Button variant="contained" color="secondary"
                            onClick={() => handleLogin()}>
                        Login
                    </Button>
                </div>
            </Grid>
            <Grid item xs className={styles.inputContainer}>
                <Link className={styles.helperText}
                      href={"/register"}
                      color={"inherit"}
                      variant={"subtitle2"}>
                    Don't have an account?
                </Link>
            </Grid>
        </Paper>
    </AuthContainer>
}

export default Login;
