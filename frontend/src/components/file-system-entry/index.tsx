import {PropsWithChildren} from "react";
import {Card, CardContent} from "@mui/material";

interface Props {
	onClick?: () => void;
}

const FileSystemEntry = ({onClick, children}: PropsWithChildren<Props>) => {
	return <Card onClick={e => onClick?.()}>
		<CardContent sx={{justifyContent: "center"}}>
			{children}
		</CardContent>
	</Card>
}

export default FileSystemEntry;
