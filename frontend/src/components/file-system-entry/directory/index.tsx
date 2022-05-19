import {observer} from "mobx-react";
import {PropsWithChildren} from "react";
import ExplorerResponse from "../../../accessors/types/explorer-response";
import FolderIcon from '@mui/icons-material/Folder';
import {Card, CardContent, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import FileSystemEntry from "../index";

interface Props {
	explorerResponse: ExplorerResponse;
}

const Directory = ({explorerResponse}: PropsWithChildren<Props>) => {
	const navigate = useNavigate();

	const handleClick = () => {
		navigate(`/explore?path=${explorerResponse.absolutePath}`)
	}

	return <FileSystemEntry onClick={handleClick} icon={<FolderIcon sx={{fontSize:100 }}/>}>
		<Typography noWrap>
			{explorerResponse.name}
		</Typography>
	</FileSystemEntry>
}

export default Directory
