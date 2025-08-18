import { API_BASE } from '../constants'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

function RegisterPage() {
    const [registerUsername, setRegisterUsername] = useState('')
    const [registerPassword, setRegisterPassword] = useState('')
    const [registerError, setRegisterError] = useState('')
    const navigate = useNavigate()

    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault()

        const res = await fetch(`${API_BASE}/auth/register`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            credentials: 'include',
            body: JSON.stringify({
                username: registerUsername,
                password: registerPassword
            })
        })

        if (res.ok) {
            navigate('/login')
        } else {
            const msg = await res.text()
            setRegisterError(msg)
        }
    }

    return (
        <section>
            <form onSubmit={handleRegister}>
                <h2>Register</h2>
                {registerError && <p style={{ color: 'red'}}>{registerError}</p>}
                <input value={registerUsername} onChange={e => setRegisterUsername(e.target.value)} placeholder="Username" />
                <input value={registerPassword} onChange={e => setRegisterPassword(e.target.value)} type="password" />
                <button type="submit">Register</button>
            </form>
            <p>Already have an account? <a href="./login">Login Here!</a></p>
        </section>
    )
}

export default RegisterPage