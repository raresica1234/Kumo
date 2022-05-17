import {
	createPathPont,
	deletePathPoint,
	getAllPathPoints,
	updatePathPoint
} from "../../../accessors/path-point-accessor";
import {makeAutoObservable, runInAction, toJS} from "mobx";
import {createContext, useState} from "react";
import PathPointModel from "../../../models/PathPointModel";

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

	public setPathPoint = (path: string) => {
		runInAction(() => {
			this.pathPoint = path;
		})
	}

	public fetchPathPoints = async () => {
		const pathPoints = await getAllPathPoints();
		runInAction(() => {
			this.dirty = false;
			this.pathPoints = pathPoints.map(pathPoint => ({...pathPoint, dirty: false, deleted: false}));
			this.pathPointsToDisplay = toJS(this.pathPoints);
		});
	}

	public modifyPathPoint = (model: PathPointModel) => {
		const index = this.pathPoints.findIndex(pathPoint => pathPoint.id === model.id);
		if (index === -1)
			// TODO: Error should be displayed
			return model;
		model.dirty = true;

		runInAction(() => {
			this.pathPoints[index] = model;
			this.dirty = true;
		})
		return model;
	}

	public markPathPointAsDeleted = (id: string, deleted: boolean) => {
		const index = this.pathPoints.findIndex(pathPoint => pathPoint.id === id);
		if (index === -1)
			return;

		runInAction(() => {
			this.pathPoints[index].deleted = deleted;
			this.pathPoints[index].dirty = true;
			this.pathPointsToDisplay[index].deleted = deleted;
			this.dirty = true;
		})
	}

	public commitChanges = async () => {
		await Promise.all(this.pathPoints.filter(pathPoint => pathPoint.deleted).map(pathPoint => deletePathPoint(pathPoint.id)));

		const dirtyPathPoints = this.pathPoints.filter(pathPoint => !pathPoint.deleted && pathPoint.dirty)

		await Promise.all(dirtyPathPoints.map(pathPoint => updatePathPoint(pathPoint)));
		runInAction(() => {
			dirtyPathPoints.forEach(dirtyPathPoint => dirtyPathPoint.dirty = false);
			this.dirty = false;
			this.pathPointsToDisplay = toJS(this.pathPoints.filter(pathPoint => !pathPoint.deleted));
		})
	}

	public addPathPoint = async () => {
		await createPathPont({path: this.pathPoint, isRoot: this.isRoot});
		await this.fetchPathPoints();
		runInAction(() => {
			this.pathPoint = "";
			this.isRoot = false;
		})
	}
}


export const pathPointManagerStore = new PathPointManagerStore()
export const PathPointManagerContext = createContext(pathPointManagerStore)
