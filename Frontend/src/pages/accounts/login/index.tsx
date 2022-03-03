import {Button, Grid, Link, Paper, TextField, Typography} from "@mui/material";
import styles from "./../accounts.module.scss"
import {useContext, useState} from "react";
import {LoginContext} from "./login-store";
import {useNavigate} from "react-router-dom";

const Login = () => {
	const navigate = useNavigate();

	const {
		setEmail,
		setPassword,
		login
	} = useContext(LoginContext)

	const [emailError, setEmailError] = useState("");
	const [passwordError, setPasswordError] = useState(<></>);

	const handleLogin = async () => {
		setEmailError("");
		setPasswordError(<></>);

		const result = await login();

		console.log(result);

		if (result == "") {
			navigate("/");
		} else {
			const errors = result.split("\n");
			errors.forEach(error => {
				if (error.toLowerCase().includes("email") || error.toLowerCase().includes("username")) {
					setEmailError(error)
				}
				if (error.toLowerCase().includes("password")) {
					setPasswordError(passwordError => <>{passwordError}{error}<br/></>);
				}
			});
		}
	}

	return <Paper elevation={1}
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
					   error={emailError !== ""}
					   helperText={emailError}
					   onChange={(e) => setEmail(e.target.value)}/>
		</Grid>
		<Grid item xs className={styles.inputContainer}>
			<TextField fullWidth
					   type="password"
					   label="Password"
					   variant="standard"
					   error={passwordError.props.children !== undefined}
					   helperText={<>{passwordError}</>}
					   onChange={(e) => setPassword(e.target.value)}/>
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
}

export default Login;
