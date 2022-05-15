export default interface ExplorerResponse {
	name: string,
	absolutePath: string,
	fileSystemEntryType: number,
	canWrite: boolean,
	canDelete: boolean
}
