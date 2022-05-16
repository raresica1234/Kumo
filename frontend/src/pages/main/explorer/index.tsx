import {ExplorerContext} from "./explorer-store";
import {PropsWithChildren, useContext, useEffect} from "react";
import {observer} from "mobx-react";
import FileSystemEntryFactory from "../../../components/file-system-entry/file-system-entry-factory";
import {Grid} from "@mui/material";
import styles from './explorer.module.scss'

interface Props {
	path?: string;
}

const Explorer = ({path}: PropsWithChildren<Props>) => {
	const {init, currentPath, fileSystemEntries} = useContext(ExplorerContext);

	useEffect(() => {
		init(path);
	}, [init, path]);

	const cards: JSX.Element[] = [];
	fileSystemEntries.forEach(entry => cards.push(
		<Grid key={entry.name} item xs>
			{FileSystemEntryFactory.create(entry)}
		</Grid>
	));

	return <Grid container direction={"row"} spacing={4} padding={4}>
		{cards}
	</Grid>
}

export default observer(Explorer);
