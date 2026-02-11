package org.thedonutdan.autolog.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.time.Period;

import java.util.List;
import java.util.UUID;

import java.io.IOException;

public class VehicleTest {
    private final UUID userId = UUID.randomUUID();
    private final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    @Test
    public void testCreateVehicleNoMaintenance() {
        Vehicle v = new Vehicle(userId, "VIN1234", "Honda", "Civic", 2015, "555555", 10000);

        assertNotNull(v.getId());
        assertEquals(userId, v.getUserId());
        assertEquals("VIN1234", v.getVIN());
        assertEquals("Honda", v.getMake());
        assertEquals("Civic", v.getModel());
        assertEquals(2015, v.getYear());
        assertEquals("555555", v.getLicensePlate());
        assertEquals(10000, v.getMileage());
    }

    @Test
    public void testCreateVehicleWithMaintenance() {
        LocalDate date = LocalDate.of(2025, 7, 1);
        ServiceType st = new ServiceType("Oil Change", 5000, Period.ofMonths(6));
        MaintenanceRecord record = new MaintenanceRecord(date, st, 10000, null, null, "5W-30");
        Vehicle v = new Vehicle(userId, "VIN1234", "Honda", "Civic", 2015, "555555", 10000, List.of(record));

        assertNotNull(v.getId());
        assertEquals(userId, v.getUserId());
        assertEquals("VIN1234", v.getVIN());
        assertEquals("Honda", v.getMake());
        assertEquals("Civic", v.getModel());
        assertEquals(2015, v.getYear());
        assertEquals("555555", v.getLicensePlate());
        assertEquals(10000, v.getMileage());
        assertEquals(List.of(record), v.getMaintenanceHistory());
    }

    @Test
    public void testAddOldMaintenance_ShouldNotUpdateMileage() {
        LocalDate date = LocalDate.of(2025, 7, 1);
        ServiceType st = new ServiceType("Oil Change", 5000, Period.ofMonths(6));
        MaintenanceRecord record = new MaintenanceRecord(date, st, 10000, null, null, "5W-30");
        Vehicle v = new Vehicle(UUID.randomUUID(), "VIN1234", "Honda", "Civic", 2015, "555555", 100000);

        v.addMaintenance(record);

        List<MaintenanceRecord> history = v.getMaintenanceHistory();
        
        assertEquals(history.get(0), record);
        assertEquals(100000, v.getMileage());
    }

    @Test
    public void testAddNewMaintenance_ShouldUpdateMileage() {
        LocalDate date = LocalDate.of(2025, 7, 1);
        ServiceType st = new ServiceType("Oil Change", 5000, Period.ofMonths(6));
        MaintenanceRecord record = new MaintenanceRecord(date, st, 100000, null, null, "5W-30");
        Vehicle v = new Vehicle(UUID.randomUUID(), "VIN1234", "Honda", "Civic", 2015, "555555", 10000);

        v.addMaintenance(record);

        List<MaintenanceRecord> history = v.getMaintenanceHistory();
        
        assertEquals(history.get(0), record);
        assertEquals(100000, v.getMileage());
    }

    @Test
    public void testSerializationDeserializationRoundTrip() throws IOException {
        LocalDate date = LocalDate.of(2025, 7, 1);
        ServiceType st = new ServiceType("Oil Change", 5000, Period.ofMonths(6));
        MaintenanceRecord record = new MaintenanceRecord(date, st, 100000, null, null, "5W-30");
        Vehicle original = new Vehicle(userId, "VIN1234", "Honda", "Civic", 2015, "555555", 10000, List.of(record));

        String json = mapper.writeValueAsString(original);
        Vehicle deserialized = mapper.readValue(json, Vehicle.class);

        assertEquals(original.getId(), deserialized.getId());
        assertEquals(original.getVIN(), deserialized.getVIN());
        assertEquals(original.getMake(), deserialized.getMake());
        assertEquals(original.getModel(), deserialized.getModel());
        assertEquals(original.getYear(), deserialized.getYear());
        assertEquals(original.getMaintenanceHistory(), deserialized.getMaintenanceHistory());
        assertEquals(original.getMileage(), deserialized.getMileage());
        assertEquals(original.getLicensePlate(), deserialized.getLicensePlate());
    }

    @Test
    public void testEquals() {
        UUID id = UUID.randomUUID();
        Vehicle v1 = new Vehicle(UUID.randomUUID(), "VIN123", "Honda", "Civic", 2020, "ABC123", 50000);
        v1.setId(id);
        
        Vehicle v2 = new Vehicle(UUID.randomUUID(), "VIN456", "Toyota", "Camry", 2021, "XYZ789", 60000);
        v2.setId(id);
        
        assertEquals(v1, v2);
        
        v2.setId(UUID.randomUUID());
        assertNotEquals(v1, v2);
    }

    @Test
    public void testHashCode() {
        UUID id = UUID.randomUUID();
        Vehicle v = new Vehicle(UUID.randomUUID(), "VIN123", "Honda", "Civic", 2020, "ABC123", 50000);
        v.setId(id);
        
        assertEquals(id.hashCode(), v.hashCode());
    }

    @Test 
    public void testToString() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Vehicle v = new Vehicle(userId, "VIN123", "Honda", "Civic", 2020, "ABC123", 50000);
        v.setId(id);
        
        String str = v.toString();
        assertTrue(str.contains(id.toString()));
        assertTrue(str.contains(userId.toString()));
        assertTrue(str.contains("VIN123"));
        assertTrue(str.contains("Honda"));
        assertTrue(str.contains("Civic"));
        assertTrue(str.contains("2020"));
        assertTrue(str.contains("ABC123"));
        assertTrue(str.contains("50000"));
    }
}