import {getJwtToken} from "../infrastructure/authenticate/token-helper";

type httpMethod = "GET" | "POST" | "PUT" | "DELETE";

const genericFetch = <T>(method: httpMethod, url: string, body?: any) =>
    new Promise<T>(async (resolve, reject) => {
        try {
            const response = await fetch(url, {
                method,
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${getJwtToken()}`
                },
                body: JSON.stringify(body)
            })

            const payload = await response.json();

            const statusCode = response.status;

            if (statusCode / 100 === 2) {
                try {
                    resolve(payload)
                } catch {
                    resolve(undefined as any)
                }
                return;
            }
            if (payload.httpStatus) {
                reject(payload.message);
            }
        } catch (exception) {
            reject(exception);
        }
    });


export const httpPost = <T>(url: string, body?: any) => genericFetch<T>("POST", url, body);
export const httpGet = <T>(url: string) => genericFetch<T>("GET", url);
export const httpDelete = <T>(url: string, id: string) => genericFetch<T>("DELETE", `${url}/${id}`);
export const httpDeleteTwoId = <T>(url: string, id1: string, id2: string) => genericFetch<T>("DELETE", `${url}/${id1}/${id2}`);

export const httpPut = <T>(url: string, body?: any) => genericFetch<T>("PUT", url, body);
