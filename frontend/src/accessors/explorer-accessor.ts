import {httpGet} from "./helper-functions";
import ExplorerResponse from "./types/explorer-response";
import {BASE_URL} from "./constants";

const EXPLORER_URL = `${BASE_URL}/Explore`

export const getRootPathPoints = () => httpGet<(ExplorerResponse[])|undefined>(EXPLORER_URL)
export const exploreLocation = (path: string) => httpGet<(ExplorerResponse[])|undefined>(`${EXPLORER_URL}?path=${path}`);
