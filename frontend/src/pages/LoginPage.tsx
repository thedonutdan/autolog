import { API_BASE } from '../constants'
import { useState, useEffect } from 'react'
import { useNavigate, Link } from 'react-router-dom'

function LoginPage() {
    useEffect(() => {
        fetch(`${API_BASE}/api/me`, { credentials : "include" })
            .then(res => {
                if (res.ok) {
                    navigate("/vehicles")
                }
            });
    }, [])
    const [loginUsername, setLoginUsername] = useState('')
    const [loginPassword, setLoginPassword] = useState('')
    const [rememberMe, setRememberMe] = useState(false)
    const [loginError, setLoginError] = useState('')
    const navigate = useNavigate()

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault()

        const res = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            credentials: 'include',
            body: JSON.stringify({
                username: loginUsername,
                password: loginPassword,
                rememberMe})
        })

        if (res.ok) {
            navigate('/vehicles')
        } else {
            const msg = await res.text()
            setLoginError(msg)
        }
    }

    const handleGuestLogin = async ()=> {
        try {
            const res = await fetch(`${API_BASE}/auth/guest`, {
                method: "POST",
                credentials: "include"
            })

            if (res.ok) {
                navigate("/vehicles")
            } else {
                console.error("guest login failed")
            }
        } catch (err) {
            console.error("error", err)
        }
    }

    return (
        <section>
            <form onSubmit={handleLogin}>
                <h2>Login</h2>
                {loginError && <p style={{ color: 'red'}}>{loginError}</p>}
                <input value={loginUsername} onChange={e => setLoginUsername(e.target.value)} placeholder="Username" />
                <input value={loginPassword} onChange={e => setLoginPassword(e.target.value)} type="password" />
                <label>
                    <input type="checkbox" checked={rememberMe} onChange={e => setRememberMe(e.target.checked)} />
                    Remember Me
                </label>
                <button type="submit">Login</button>
            </form>
            <p>Don't have an account? <Link to="/register">Register</Link></p>
            <button onClick={handleGuestLogin}>
                Continue as guest
            </button>
        </section>
    )
}

export default LoginPage