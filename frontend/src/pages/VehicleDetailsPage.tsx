import { useParams, useLocation, useNavigate } from "react-router-dom"
import type { Vehicle } from "../types/Vehicle"
import MaintenanceRecordList from "../components/MaintenanceRecordList"
import type { MaintenanceRecordDTO } from "../types/MaintenanceRecordDTO"
import { useState, useEffect } from "react"
import type { MaintenanceRecord } from "../types/MaintenanceRecord"
import { fetchWithAuth } from "../utils/fetchHandlers"
import { AuthError, toTypedHttpError } from "../utils/errors"

type CreateMaintenanceRecordFormProps = {
    onSubmit: (data: MaintenanceRecordDTO) => void
    submitting: boolean
    error?: string | null
}

function CreateMaintenanceRecordForm({ onSubmit, submitting = false, error = null}: CreateMaintenanceRecordFormProps) {
    const [date, setDate] = useState('')
    const [name, setName] = useState('')
    const [expiryMiles, setExpiryMiles] = useState<string | null>(null)
    const [expiryTime, setExpiryTime] = useState<string | null>(null)
    const [mileage, setMileage] = useState('')
    const [notes, setNotes] = useState('')

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault()

        const dto: MaintenanceRecordDTO = {
            date: date,
            name: name,
            expiryMiles: Number(expiryMiles),
            expiryTime: expiryTime,
            mileage: Number(mileage),
            notes: notes
        }

        onSubmit(dto)
    }

    return (
        <section>
            <form onSubmit={handleSubmit}>
                <h2>Add Maintenance</h2>
                <fieldset disabled={submitting} style={{ display: 'grid', gap: 8}}>
                    {error && <p style={{ color: 'red'}}>{error}</p>}
                    <label>
                        Date
                        <input type="date" value={date} onChange={e => setDate(e.target.value)} required />
                    </label>
                    <label>
                        Service Type
                        <input type= "text" value={name} onChange={e => setName(e.target.value)} required />
                    </label>
                    <label>
                        Mileage
                        <input type="number" inputMode="numeric" value={mileage} onChange={e => setMileage(e.target.value)} required />
                    </label>
                    <label>
                        Expiry Miles (Optional)
                        <input type="number" inputMode="numeric" value={expiryMiles ?? ""} onChange={e => {
                            const val = e.target.value
                            setExpiryMiles(val === "" ? null : val)}} />
                    </label>
                    <label>
                        Expiry Time (Optional)
                        <input type="text" value={expiryTime ?? ""} onChange={e => {
                            const val = e.target.value
                            setExpiryTime(val === "" ? null : val)}} placeholder="P6M, P1Y, etc" />
                    </label>
                    <label>
                        Notes
                        <textarea value={notes} onChange={e => setNotes(e.target.value)} rows={3} />
                    </label>
                    <button type="submit">{submitting ?'Adding record...' : 'Add Record'}</button>
                </fieldset>
            </form>
        </section>
    )
}

export default function VehicleDetailsPage() {
    const { id } = useParams()
    const location = useLocation()
    const vehicleFromState = (location.state as { vehicle?: Vehicle })?.vehicle
    const [header, setHeader] = useState(() =>
        vehicleFromState
          ? {
              year: vehicleFromState.year,
              make: vehicleFromState.make,
              model: vehicleFromState.model,
              vin: vehicleFromState.vin,
              licensePlate: vehicleFromState.licensePlate,
              mileage: vehicleFromState.mileage,
            }
          : null
      )
    const [records, setRecords] = useState<MaintenanceRecord[]>(
        vehicleFromState?.maintenanceHistory ?? []
    )
    const [loading, setLoading] = useState(!vehicleFromState)
    const [error, setError] = useState<string | null>(null)
    const [submitting, setSubmitting] = useState(false)
    const [createMaintenanceRecordError, setCreateMaintenanceRecordError] = useState<string | null>(null)
    const navigate = useNavigate()

    useEffect(() => {
        if (vehicleFromState) return;
        let cancelled = false;
        
        (async () => {
            try {
                setLoading(true)
                const res = await fetchWithAuth(`/vehicles/${id}`)
                
                if (!res.ok) throw toTypedHttpError(res)
                const v = (await res.json()) as Vehicle
                if (!cancelled) {
                    setHeader({
                    year: v.year, make: v.make, model: v.model,
                    vin: v.vin, licensePlate: v.licensePlate, mileage: v.mileage,
                    });
                    setRecords(v.maintenanceHistory ?? [])
                }
            } catch (e: any) {
                if (e instanceof AuthError) {
                    navigate('/login', { replace: true })
                }
                if (!cancelled) setError(e.message ?? 'Failed to load vehicle');
            } finally {
                if (!cancelled) setLoading(false)
            }
        })();
        
        return () => { cancelled = true; }
        }, [id, vehicleFromState])

    const handleCreateMaintenanceRecord = async (data: MaintenanceRecordDTO) => {
        setSubmitting(true)
        
        try {
            const res = await fetchWithAuth(`/vehicles/${id}/records`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            })

            if (!res.ok) throw toTypedHttpError(res)
            
            const newRecord = (await res.json()) as MaintenanceRecord
            if (!newRecord) throw new Error('Malformed MaintenanceRecord in response')
            
            setRecords(prev => [...prev, newRecord])
            setSubmitting(false)
            setCreateMaintenanceRecordError(null)
        } catch (e: any) {
            if (e instanceof AuthError) {
                navigate('/login', { replace: true })
            }
            setCreateMaintenanceRecordError(e?.message ?? "Failed to create maintenance record")
        } finally {
            setSubmitting(false)
        }
    }

    return (
        <main>
            <h1>Vehicle Details</h1>

            {loading ? (
                <p>Loading…</p>
            ) : error ? (
                <p style={{ color: 'red' }}>{error}</p>
            ) : header ? (
                <>
                <h2>{header.year} {header.make} {header.model}</h2>

                <CreateMaintenanceRecordForm
                    onSubmit={handleCreateMaintenanceRecord}
                    submitting={false}
                    error={createMaintenanceRecordError} />

                <section>
                    <MaintenanceRecordList records={records} />
                </section>
                </>
            ) : (
                <p>Not found.</p>
            )}
            </main>
    )
}