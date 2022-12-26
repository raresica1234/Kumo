import {observer} from "mobx-react";
import styles from "./../accounts.module.scss"
import {Button, Grid, Link, Paper, TextField, Typography} from "@mui/material";
import {useContext, useState} from "react";
import {RegisterContext} from "./register-store";
import {useNavigate} from "react-router-dom";
import AuthContainer from "../../../components/containers/auth-container";

const Register = () => {
    const navigate = useNavigate();

    const {
        setUsername,
        setEmail,
        setPassword,
        setConfirmPassword,
        register
    } = useContext(RegisterContext)

    const initErrorStates = () => {
        return {
            usernameError: <></>,
            emailError: <></>,
            passwordError: <></>,
            confirmPasswordError: <></>,
        };
    }

    const [errors, setErrors] = useState(initErrorStates());

    const handleRegister = async () => {

        const result = await register();

        if (result === "") {
            // Successful login
            navigate("/login");
        } else {
            let currentErrors = initErrorStates();

            const errors = result.split("\n");
            errors.forEach(error => {
                if (error.toLowerCase().includes("username"))
                    currentErrors.usernameError = <>{currentErrors.usernameError}{error}<br/></>;

                if (error.toLowerCase().includes("email"))
                    currentErrors.emailError = <>{currentErrors.emailError}{error}<br/></>;

                if (error.toLowerCase().includes("match"))
                    currentErrors.confirmPasswordError = <>{currentErrors.confirmPasswordError}{error}<br/></>;
                else if (error.toLowerCase().includes("password"))
                    currentErrors.passwordError = <>{currentErrors.passwordError}{error}<br/></>;
            })

            setErrors(currentErrors);
        }
    }

    const handleEnter = async (key: string) => {
        if (key === "Enter")
            await handleRegister();
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
                <Typography variant="h3">Register</Typography>
            </Grid>
            <Grid item className={styles.inputContainer}>
                <TextField fullWidth
                           label="Username"
                           variant="standard"
                           type="text"
                           error={errors.usernameError.props.children !== undefined}
                           helperText={errors.usernameError}
                           onChange={(e) => setUsername(e.target.value)}
                           onKeyDown={(e) => handleEnter(e.key)}/>
            </Grid>

            <Grid item className={styles.inputContainer}>
                <TextField fullWidth
                           label="Email"
                           variant="standard"
                           type="email"
                           error={errors.emailError.props.children !== undefined}
                           helperText={errors.emailError}
                           onChange={(e) => setEmail(e.target.value)}
                           onKeyDown={(e) => handleEnter(e.key)}/>
            </Grid>
            <Grid item xs className={styles.inputContainer}>
                <TextField fullWidth
                           type="password"
                           label="Password"
                           variant="standard"
                           error={errors.passwordError.props.children !== undefined}
                           helperText={<>{errors.passwordError}</>}
                           onChange={(e) => setPassword(e.target.value)}
                           onKeyDown={(e) => handleEnter(e.key)}/>
            </Grid>
            <Grid item xs className={styles.inputContainer}>
                <TextField fullWidth
                           type="password"
                           label="Confirm Password"
                           variant="standard"
                           error={errors.confirmPasswordError.props.children !== undefined}
                           helperText={errors.confirmPasswordError}
                           onChange={(e) => setConfirmPassword(e.target.value)}
                           onKeyDown={(e) => handleEnter(e.key)}/>
            </Grid>
            <Grid item xs className={styles.inputContainer}>
                <div className={styles.submit}>
                    <Button variant="contained" color="secondary" onClick={() => handleRegister()}>
                        Register
                    </Button>
                </div>
            </Grid>
            <Grid item xs className={styles.inputContainer}>
                <Link className={styles.helperText} href={"/login"} color={"inherit"} variant={"subtitle2"}>Already have
                    an
                    account?</Link>
            </Grid>
        </Paper>
    </AuthContainer>
}

export default observer(Register);
