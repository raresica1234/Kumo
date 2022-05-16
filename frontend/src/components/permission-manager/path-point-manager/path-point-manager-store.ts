import {getAllPathPoints} from "../../../accessors/path-point-accessor";
import {runInAction} from "mobx";
import PathPoint from "../../../accessors/types/path-point";
import {createContext} from "react";

class PathPointManagerStore {
	public pathPoints: PathPoint[] = [];

	public init = () => {
		this.fetchPathPoints();
	}

	public fetchPathPoints = () => {
		getAllPathPoints().then(pathPoints => {
			runInAction(() => {
				this.pathPoints = pathPoints;
			});
		})
	}
}


export const pathPointManagerStore = new PathPointManagerStore()
export const PathPointManagerContext = createContext(pathPointManagerStore)
