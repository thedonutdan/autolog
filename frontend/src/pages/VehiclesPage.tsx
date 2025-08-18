import { API_BASE } from '../constants'
import { useEffect, useState } from 'react'
import VehicleList from '../components/VehicleList'
import type { Vehicle } from '../types/Vehicle'
import { fetchWithAuth } from '../utils/fetchHandlers'
import { useNavigate } from 'react-router-dom'
import { AuthError, NotFoundError, ServerError } from '../utils/errors'

type CreateVehicleFormProps = {
    onSubmit: (data: Vehicle) => void
    submitting: boolean
    error?: string | null
}

function CreateVehicleForm({ onSubmit, submitting = false, error = null }: CreateVehicleFormProps ) {
    const [vin, setVin] = useState('')
    const [make, setMake] = useState('')
    const [model, setModel] = useState('')
    const [year, setYear] = useState('')
    const [licensePlate, setLicensePlate] = useState('')
    const [mileage, setMileage] = useState('')

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault()

        onSubmit({
            id: '',
            vin: vin.trim().toUpperCase(),
            make: make.trim(),
            model: model.trim(),
            year: Number(year),
            licensePlate: licensePlate.trim(),
            mileage: Number(mileage),
            maintenanceHistory: [],
        })
    }

    return (
        <section>
            <form onSubmit={handleSubmit}>
                <h2>Add Vehicle</h2>
                <fieldset disabled={submitting} style={{ display: 'grid', gap: 8}}>
                    {error && <p style={{ color: 'red'}}>{error}</p>}
                    <input value={vin} onChange={e => setVin(e.target.value)} placeholder='VINABC123' />
                    <input value={make} onChange={e => setMake(e.target.value)} placeholder='Make' />
                    <input value={model} onChange={e => setModel(e.target.value)} placeholder='Model' />
                    <input value={year} onChange={e => setYear(e.target.value)} placeholder="Year" />
                    <input value={licensePlate} onChange={e => setLicensePlate(e.target.value)} placeholder='License Plate' />
                    <input value={mileage} onChange={e => setMileage(e.target.value)} placeholder='Mileage' />
                    <button type="submit">{submitting ?'Creating...' : 'Create'}</button>
                </fieldset>
            </form>
        </section>
    )
}

export default function VehiclesPage() {
    const [vehicles, setVehicles] = useState<Vehicle[]>([])
    const [submitting, setSubmitting] = useState(false)
    const [createVehicleError, setCreateVehicleError] = useState<string | null>()
    const navigate = useNavigate()

    useEffect(() => {
        const controller = new AbortController();
      
        (async () => {
          try {
            const res = await fetchWithAuth(`/vehicles`, { signal: controller.signal });
            const data = await res.json();
            setVehicles(data);
          } catch (err: any) {
            if (err.name === "AbortError") return; // ignore unmount aborts
      
            if (err instanceof AuthError) {
              // session expired → send to login
              navigate("/login", { replace: true });
            } else if (err instanceof NotFoundError) {
              // optional: show an empty state or 404 UI
              setVehicles([]);
            } else if (err instanceof ServerError) {
              // optional: toast/retry UI
              console.error("Server error:", err);
            } else {
              // fallback for other HTTP codes/network errors
              console.error(err);
            }
          }
        })();
      
        return () => controller.abort();
      }, [navigate]);

    const handleCreate = async (data: Vehicle) => {
        setSubmitting(true)
        setCreateVehicleError(null)
        try {
            const res = await fetch(`${API_BASE}/vehicles`, {
                method: 'POST',
                credentials: 'include',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            })
            if (!res.ok) throw new Error(`HTTP ${res.status} ${res.statusText}`)
            
            const vehicleId = res.headers.get('X-Vehicle-Id')
            if (!vehicleId) throw new Error('Missing X-Vehicle-Id header')
            
            setVehicles(prev => [{...data, id: vehicleId}, ...prev])
        } catch (e: any) {
            setCreateVehicleError(e?.message ?? 'Failed to create vehicle')
        } finally {
            setSubmitting(false)
        }
    }

    return (
        <main>
            <h1>Your Vehicles</h1>
            <CreateVehicleForm onSubmit={handleCreate} submitting={submitting} error={createVehicleError} />
            <VehicleList vehicles={vehicles} />
        </main>
    )

}