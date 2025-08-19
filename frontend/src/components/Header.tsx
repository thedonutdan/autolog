import { useNavigate, Link } from "react-router-dom"
import { toTypedHttpError } from "../utils/errors"
import { fetchWithAuth } from "../utils/fetchHandlers"

export default function Header() {
    const navigate = useNavigate()
    const handleLogout = async () => {
        const res = await fetchWithAuth("/auth/logout", {
            method: 'POST'
        })
        if (!res.ok) throw toTypedHttpError(res)
        navigate('/login', { replace: true })
    }
    return (
        <header>
            <Link to="/vehicles">Home</Link>
            <button onClick={handleLogout}>Logout</button>
        </header>
    )
}