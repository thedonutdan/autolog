import type { Vehicle } from "../types/Vehicle"
import { useNavigate } from "react-router-dom"

type Props = {
    vehicle: Vehicle
}

export default function VehicleCard({ vehicle }: Props) {
    const navigate = useNavigate()
    return (
        <li 
            className="vehicle-card" 
            onClick={() =>
                navigate(`/vehicles/${vehicle.id}`, {
                    state: { vehicle }
                })
            }>
            <h2>{vehicle.year} {vehicle.make} {vehicle.model}</h2>
            <p>VIN: {vehicle.vin}</p>
            <p>Plate: {vehicle.licensePlate}</p>
            <p>Mileage: {vehicle.mileage}</p>
        </li>
    )
}