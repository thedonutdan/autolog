import type { ServiceType } from './ServiceType.ts'

export interface MaintenanceRecordDTO {
    date: string
    serviceTypeName: string
    mileage: number
    expiryMiles: number | null
    expiryTime: string | null
    notes: string
}