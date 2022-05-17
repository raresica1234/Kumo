import PathPoint from "../accessors/types/path-point";

export default interface PathPointModel extends PathPoint {
	dirty: boolean;
	deleted: boolean;
}
