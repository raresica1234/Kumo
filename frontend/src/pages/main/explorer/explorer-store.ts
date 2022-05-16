import {makeAutoObservable, runInAction} from "mobx";
import {exploreLocation, getRootPathPoints} from "../../../accessors/explorer-accessor";
import {createContext} from "react";
import ExplorerResponse from "../../../accessors/types/explorer-response";

export class ExplorerStore {
	public currentPath?: string = undefined;
	public fileSystemEntries: ExplorerResponse[] = [];

	constructor() {
		makeAutoObservable(this)
	}

	public init = (path?: string) => {
		this.currentPath = path;
		if (this.currentPath === undefined)
			this.fetchPathPoints();
		else
			this.exploreLocation();
	}

	private exploreLocation = () => {
		exploreLocation(this.currentPath!).then(rootPaths => {
			runInAction(() => this.fileSystemEntries = rootPaths);
		})
	}

	private fetchPathPoints = () => {
		getRootPathPoints().then(rootPaths => {
			runInAction(() => this.fileSystemEntries = rootPaths);
		})
	}
}

export const explorerStore = new ExplorerStore();
export const ExplorerContext = createContext(explorerStore)
