import {
	createPathPont,
	deletePathPoint,
	getAllPathPoints,
	updatePathPoint
} from "../../../accessors/path-point-accessor";
import {makeAutoObservable, runInAction, toJS} from "mobx";
import {createContext} from "react";
import PathPointModel from "../../../models/path-point-model";
import {debounce} from "@mui/material";
import {toastServiceStore} from "../../../infrastructure/toast-service/toast-service-store";

class PathPointManagerStore {
	public pathPoints: PathPointModel[] = [];
	public dirty: boolean = false;
	public pathPointsToDisplay: PathPointModel[] = [];
	public isRoot: boolean = false;
	public pathPoint: string = "";

	constructor() {
		makeAutoObservable(this);
	}

	public init = () => {
		this.fetchPathPoints();
	}

	public setIsRoot = (checked: boolean) => {
		runInAction(() => {
			this.isRoot = checked;
		})
	}

	public setPathPoint = debounce((path: string) => {
		this.pathPoint = path;
	}, 400)

	public fetchPathPoints = async () => {
		try {
			const pathPoints = await getAllPathPoints();
			runInAction(() => {
				this.dirty = false;
				console.log(typeof pathPoints);
				console.log(pathPoints);
				this.pathPoints = pathPoints.map(pathPoint => ({...pathPoint, dirty: false, deleted: false}));
				this.pathPointsToDisplay = toJS(this.pathPoints);
			});
		} catch (e: any) {
			toastServiceStore.showError(e.toString());
		}
	}

	public modifyPathPoint = (model: PathPointModel) => {
		const index = this.pathPoints.findIndex(pathPoint => pathPoint.id === model.id);
		if (index === -1) {
			toastServiceStore.showError("Could not find path point!");
			return model;
		}
		model.dirty = true;

		runInAction(() => {
			this.pathPoints[index] = model;
			this.dirty = true;
		})
		return model;
	}

	public markPathPointAsDeleted = (id: string, deleted: boolean) => {
		const index = this.pathPoints.findIndex(pathPoint => pathPoint.id === id);
		if (index === -1) {
			toastServiceStore.showError("Could not find path point!");
			return;
		}

		runInAction(() => {
			this.pathPoints[index].deleted = deleted;
			this.pathPoints[index].dirty = true;
			this.pathPointsToDisplay[index].deleted = deleted;
			this.dirty = true;
		})
	}

	public commitChanges = async () => {
		try {
			await Promise.all(this.pathPoints.filter(pathPoint => pathPoint.deleted).map(pathPoint => deletePathPoint(pathPoint.id)));

			const dirtyPathPoints = this.pathPoints.filter(pathPoint => !pathPoint.deleted && pathPoint.dirty)

			await Promise.all(dirtyPathPoints.map(pathPoint => updatePathPoint(pathPoint)));
			runInAction(() => {
				dirtyPathPoints.forEach(dirtyPathPoint => dirtyPathPoint.dirty = false);
				this.dirty = false;
				this.pathPointsToDisplay = toJS(this.pathPoints.filter(pathPoint => !pathPoint.deleted));
			})
			toastServiceStore.showSuccess("Changes successfully committed!");
		} catch (e: any) {
			toastServiceStore.showError(e.toString());
		}
	}

	public addPathPoint = async () => {
		try {
			await createPathPont({path: this.pathPoint, isRoot: this.isRoot});
			await this.fetchPathPoints();
			runInAction(() => {
				this.pathPoint = "";
				this.isRoot = false;
			})
			toastServiceStore.showSuccess("Path point successfully added!");
		} catch (e: any) {
			toastServiceStore.showError(e);
		}
	}
}


export const pathPointManagerStore = new PathPointManagerStore()
export const PathPointManagerContext = createContext(pathPointManagerStore)
