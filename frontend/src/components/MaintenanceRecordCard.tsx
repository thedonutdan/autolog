import type { MaintenanceRecord } from "../types/MaintenanceRecord"

type Props = {
    record: MaintenanceRecord
}

export default function MaintenanceRecordCard({ record }: Props) {
    return (
        <li className="maintenance-record-card">
           <h2>{record.serviceType.name}</h2>
           <p>Service Date: {record.date}</p>
           <p>Mileage: {record.mileage}</p>
           {record.expiryMileage &&<p>Expires at {record.expiryMileage} miles</p>}
           {record.expiryDate && <p>Expires on {record.expiryDate}</p>}
           <p>Notes: {record.notes}</p>
        </li>
    )
}