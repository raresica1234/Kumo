import {httpGet} from "./helper-functions";
import ExplorerResponse from "./types/explorer-response";
import {BASE_URL} from "./constants";

const EXPLORER_URL = `${BASE_URL}/Explore`

// export const getRootPathPoints = () => httpGet<ExplorerResponse[]>(EXPLORER_URL)
// export const exploreLocation = (path: string) => httpGet<ExplorerResponse[]>(`${EXPLORER_URL}?path=${path}`);
