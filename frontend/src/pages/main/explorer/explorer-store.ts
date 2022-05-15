import {makeAutoObservable, runInAction} from "mobx";
import {getRootPathPoints} from "../../../accessors/explorer-accessor";
import {createContext} from "react";
import ExplorerResponse from "../../../accessors/types/explorer-response";

export class ExplorerStore {
	public currentPath?: string = undefined;
	public paths: ExplorerResponse[] = [];

	constructor() {
		makeAutoObservable(this)
	}

	public init = () => {
		this.currentPath = "";
		this.fetchPathPoints();
	}

	private fetchPathPoints = () => {
		getRootPathPoints().then(rootPaths => {
			runInAction(() => this.paths = rootPaths);
		})
	}
}

export const explorerStore = new ExplorerStore();
export const ExplorerContext = createContext(explorerStore)
