import ExplorerResponse from "../../accessors/types/explorer-response";
import {FileSystemEntryType} from "../../accessors/types/file-system-entry-type";
import Directory from "./directory";
import File from "./file";

export default class FileSystemEntryFactory {
	public static create(explorerResponse: ExplorerResponse): JSX.Element {
		const fileSystemEntryType: FileSystemEntryType = explorerResponse.fileSystemEntryType as FileSystemEntryType;

		switch (fileSystemEntryType) {
			case FileSystemEntryType.Unknown:
				return <></>
			case FileSystemEntryType.Directory:
				return <Directory explorerResponse={explorerResponse}/>
			case FileSystemEntryType.File:
				return <File explorerResponse={explorerResponse}/>
		}
	}
}
