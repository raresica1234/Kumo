import {observer} from "mobx-react";
import styles from "./../accounts.module.scss"
import {Button, Grid, Link, Paper, TextField, Typography} from "@mui/material";
import {useContext, useState} from "react";
import {RegisterContext} from "./register-store";
import {useNavigate} from "react-router-dom";

const Register = () => {
	const navigate = useNavigate();

	const {
		setEmail,
		setPassword,
		setConfirmPassword,
		register
	} = useContext(RegisterContext)

	const [emailError, setEmailError] = useState("");
	const [passwordError, setPasswordError] = useState(<></>);
	const [confirmPasswordError, setConfirmPasswordError] = useState("");

	const handleRegister = async () => {
		setEmailError("");
		setPasswordError(<></>);
		setConfirmPasswordError("");

		const result = await register();

		if (result === "") {
			// Successful login
			navigate("/login");
		} else {
			const errors = result.split("\n");
			errors.forEach(error => {
				if (error.toLowerCase().includes("email") || error.toLowerCase().includes("username")) {
					setEmailError(error)
				}
				if (error.toLowerCase().includes("match")) {
					setConfirmPasswordError(error);
				} else if (error.toLowerCase().includes("password")) {
					setPasswordError(passwordError => <>{passwordError}{error}<br/></>);
				}
			})
		}
	}

	return <Paper elevation={1}
				  component={Grid} container
				  spacing={2}
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
					   label="Email"
					   variant="standard"
					   type="email"
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
			<TextField fullWidth
					   type="password"
					   label="Confirm Password"
					   variant="standard"
					   error={confirmPasswordError !== ""} helperText={confirmPasswordError}
					   onChange={(e) => setConfirmPassword(e.target.value)}/>
		</Grid>
		<Grid item xs className={styles.inputContainer}>
			<div className={styles.submit}>
				<Button variant="contained" color="secondary" onClick={() => handleRegister()}>
					Register
				</Button>
			</div>
		</Grid>
		<Grid item xs className={styles.inputContainer}>
			<Link className={styles.helperText} href={"/login"} color={"inherit"} variant={"subtitle2"}>Already have an
				account?</Link>
		</Grid>
	</Paper>
}

export default observer(Register);
