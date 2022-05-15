import {ExplorerContext} from "./explorer-store";
import {useContext, useEffect} from "react";
import {observer} from "mobx-react";

const Explorer = () => {
	const {init, currentPath, paths} = useContext(ExplorerContext);

	useEffect(() => {
		init();
	}, [init]);

	return <>
		Current path: {currentPath} <br/>
	</>
}

export default observer(Explorer);
