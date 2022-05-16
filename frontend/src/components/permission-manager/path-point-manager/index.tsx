import {useContext, useEffect} from "react";
import {PathPointManagerContext} from "./path-point-manager-store";

const PathPointManager = () => {
	const {pathPoints, init, fetchPathPoints} = useContext(PathPointManagerContext);

	useEffect(() => {
		init()
	}, [init]);

	console.log(pathPoints);

	return <>
	</>
}

export default PathPointManager;
