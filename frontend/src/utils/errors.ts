export class HttpError extends Error {
    public status: number
    constructor(message: string, status: number) {
        super(message)
        this.name = "HttpError"
        this.status = status
    }
}

export class AuthError extends HttpError {
    constructor(message = "Unauthorized") {
        super(message, 401)
        this.name = "AuthError"
    }
}

export class NotFoundError extends HttpError {
    constructor(message = "Not Found") {
        super(message, 404)
        this.name = "NotFoundError"
    }
}

export class ServerError extends HttpError {
    constructor(message = "Server Error") {
        super(message, 500)
        this.name = "ServerError"
    }
}

/**
 * Resolves an HTTP response error into an internally typed error
 * @param res HTTP response
 * @returns Internally typed HTTP error
 */
export function toTypedHttpError(res: Response): HttpError {
    const msg = `HTTP ${res.status} ${res.statusText}`
    if (res.status === 401) return new AuthError(msg)
    if (res.status === 404) return new NotFoundError(msg)
    if (res.status >= 500) return new ServerError(msg)
    return new HttpError(msg, res.status) // Generic HTTP error if no specifics are matched
}