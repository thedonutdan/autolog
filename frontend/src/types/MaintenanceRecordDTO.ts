import type { ServiceType } from './ServiceType.ts'

export interface MaintenanceRecordDTO {
    date: string
    name: string
    mileage: number
    expiryMiles: number | null
    expiryTime: string | null
    notes: string
}