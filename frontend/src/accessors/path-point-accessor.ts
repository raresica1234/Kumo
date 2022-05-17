import {BASE_URL} from "./constants";
import {httpDelete, httpGet, httpPost, httpPut} from "./helper-functions";
import PathPoint from "./types/path-point";
import PathPointCreate from "./types/path-point-create";

const PATH_POINT_URL = `${BASE_URL}/PathPoint`

export const getAllPathPoints = () => httpGet<PathPoint[]>(PATH_POINT_URL)
export const createPathPont = (pathPoint: PathPointCreate) => httpPost(PATH_POINT_URL, pathPoint);
export const updatePathPoint = (pathPoint: PathPoint) => httpPut(PATH_POINT_URL, pathPoint);
export const deletePathPoint = (pathPointId: string) => httpDelete(PATH_POINT_URL, pathPointId);
