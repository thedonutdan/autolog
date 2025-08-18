import { API_BASE } from "../constants"
import { toTypedHttpError } from "./errors"

/**
 * Makes a fetch call with credentials included. Returns an internally typed HttpError upon failure
 * @param url url endpoint to call, excludes API_BASE
 * @param options Options for request
 * @returns HTTP Response or internally typed HttpError upon failure
 */
export async function fetchWithAuth(url: string, options: RequestInit = {}) {
    const res = await fetch(`${API_BASE}${url}`, {
        ...options,
        credentials: 'include',
    })

    if (!res.ok) throw toTypedHttpError(res)

    return res
}