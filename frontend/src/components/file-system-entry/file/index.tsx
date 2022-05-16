import FileSystemEntry from "../index";
import {Typography} from "@mui/material";
import ExplorerResponse from "../../../accessors/types/explorer-response";
import {PropsWithChildren} from "react";
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile';

interface Props {
	explorerResponse: ExplorerResponse;
}

const File = ({explorerResponse}: PropsWithChildren<Props>) => {
	return <FileSystemEntry>
		<InsertDriveFileIcon sx={{fontSize: 100}}/>
		<Typography>
			{explorerResponse.name}
		</Typography>
	</FileSystemEntry>
}

export default File;
