package org.thedonutdan.autolog.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import org.thedonutdan.autolog.model.Vehicle;
import org.thedonutdan.autolog.DTO.VehicleDTO;
import java.util.UUID;

public class VehicleDTOTest {

    @Test
    public void testFromEntity() {
        UUID userId = UUID.randomUUID();
        Vehicle vehicle = new Vehicle(
            userId,
            "VIN123",
            "Honda",
            "Civic",
            2020,
            "ABC123",
            50000
        );
        
        VehicleDTO dto = VehicleDTO.from(vehicle);
        
        assertEquals(vehicle.getId(), dto.getId());
        assertEquals(vehicle.getVIN(), dto.getVIN());
        assertEquals(vehicle.getMake(), dto.getMake());
        assertEquals(vehicle.getModel(), dto.getModel());
        assertEquals(vehicle.getYear(), dto.getYear());
        assertEquals(vehicle.getLicensePlate(), dto.getLicensePlate());
        assertEquals(vehicle.getMileage(), dto.getMileage());
    }

    @Test
    public void testToEntity() {
        UUID userId = UUID.randomUUID();
        VehicleDTO dto = new VehicleDTO();
        dto.setVIN("VIN123");
        dto.setMake("Honda");
        dto.setModel("Civic");
        dto.setYear(2020);
        dto.setLicensePlate("ABC123");
        dto.setMileage(50000);
        
        Vehicle vehicle = Vehicle.from(dto, userId);
        
        assertEquals(dto.getId(), vehicle.getId());
        assertEquals(dto.getVIN(), vehicle.getVIN());
        assertEquals(dto.getMake(), vehicle.getMake());
        assertEquals(dto.getModel(), vehicle.getModel());
        assertEquals(dto.getYear(), vehicle.getYear());
        assertEquals(dto.getLicensePlate(), vehicle.getLicensePlate());
        assertEquals(dto.getMileage(), vehicle.getMileage());
    }

    @Test
    public void testRoundTrip() {
        UUID userId = UUID.randomUUID();
        Vehicle original = new Vehicle(
            userId,
            "VIN123",
            "Honda",
            "Civic",
            2020,
            "ABC123",
            50000
        );
        
        VehicleDTO dto = VehicleDTO.from(original);
        Vehicle converted = Vehicle.from(dto, userId);
        
        assertEquals(original.getId(), converted.getId());
        assertEquals(original.getUserId(), converted.getUserId());
        assertEquals(original.getVIN(), converted.getVIN());
        assertEquals(original.getMake(), converted.getMake());
        assertEquals(original.getModel(), converted.getModel());
        assertEquals(original.getYear(), converted.getYear());
        assertEquals(original.getLicensePlate(), converted.getLicensePlate());
        assertEquals(original.getMileage(), converted.getMileage());
    }
}