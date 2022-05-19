import {PropsWithChildren} from "react";
import {Card, CardContent} from "@mui/material";

import styles from './file-system-entry.module.scss'

interface Props {
	onClick?: () => void;
	icon: JSX.Element
}

const FileSystemEntry = ({onClick, icon, children}: PropsWithChildren<Props>) => {
	return <Card elevation={12} onClick={e => onClick?.()}>
		<CardContent>
			<div className={styles.container}>
				{icon}
			</div>
			{children}
		</CardContent>
	</Card>
}

export default FileSystemEntry;
