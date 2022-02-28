import {observer} from "mobx-react";
import styles from "./register.module.scss"
import {Button, Container, Grid, TextField, Typography} from "@mui/material";
import {useContext, useState} from "react";
import {RegisterContext} from "./register-store";
import {useNavigate} from "react-router-dom";

const Register = () => {
	const navigate = useNavigate();

	const {
		user,
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
		}
		else {
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

	return <Container
		className={styles.container}
		maxWidth="xs">
		<Grid
			container
			spacing={2}
			direction="column"
			alignItems="center"
			maxWidth="xs"
			justifyContent="center">
			<Grid item>
				<Typography variant="h3">Register</Typography>
			</Grid>
			<Grid item className={styles.inputContainer}>
				<TextField fullWidth label="Email" variant="standard" type="email" error={emailError !== ""}
						   helperText={emailError}
						   onChange={(e) => setEmail(e.target.value)}/> <br/>
			</Grid>
			<Grid item xs className={styles.inputContainer}>
				<TextField type="password" fullWidth label="Password" variant="standard"
						   error={passwordError.props.children !== undefined}
						   helperText={<>{passwordError}</>}
						   onChange={(e) => setPassword(e.target.value)}/> <br/>
			</Grid>
			<Grid item xs className={styles.inputContainer}>
				<TextField type="password" fullWidth label="Confirm Password" variant="standard"
						   error={confirmPasswordError !== ""} helperText={confirmPasswordError}
						   onChange={(e) => setConfirmPassword(e.target.value)}/> <br/>
			</Grid>
			<Grid item xs className={styles.inputContainer}>
				<div className={styles.submit}>
					<Button variant="contained" onClick={(e) => handleRegister()}>
						Register
					</Button>
				</div>
			</Grid>
		</Grid>
	</Container>
}

export default observer(Register);
