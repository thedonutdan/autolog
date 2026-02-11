package org.thedonutdan.autolog.validation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import org.thedonutdan.autolog.model.Vehicle;

import java.util.List;
import java.util.UUID;
import java.time.Year;

public class VehicleValidatorTest {

    @Test
    public void testValidVehicle() {
        Vehicle v = new Vehicle(
            UUID.randomUUID(),
            "VIN123",
            "Honda",
            "Civic",
            2020,
            "ABC123",
            50000
        );
        
        List<String> errors = VehicleValidator.validate(v);
        assertTrue(errors.isEmpty());
    }

    @Test 
    public void testInvalidMake() {
        Vehicle v = new Vehicle(
            UUID.randomUUID(),
            "VIN123",
            "",
            "Civic",
            2020,
            "ABC123",
            50000
        );

        List<String> errors = VehicleValidator.validate(v);
        assertEquals(1, errors.size());
        assertTrue(errors.contains("Make is required."));
    }

    @Test
    public void testInvalidModel() {
        Vehicle v = new Vehicle(
            UUID.randomUUID(),
            "VIN123", 
            "Honda",
            null,
            2020,
            "ABC123",
            50000
        );

        List<String> errors = VehicleValidator.validate(v);
        assertEquals(1, errors.size());
        assertTrue(errors.contains("Model is required."));
    }

    @Test
    public void testInvalidYear() {
        Vehicle v = new Vehicle(
            UUID.randomUUID(),
            "VIN123",
            "Honda", 
            "Civic",
            1800,
            "ABC123",
            50000
        );

        List<String> errors = VehicleValidator.validate(v);
        assertEquals(1, errors.size());
        assertTrue(errors.contains("Year is out of range."));

        v = new Vehicle(
            UUID.randomUUID(),
            "VIN123",
            "Honda",
            "Civic", 
            Year.now().getValue() + 2,
            "ABC123",
            50000
        );

        errors = VehicleValidator.validate(v);
        assertEquals(1, errors.size());
        assertTrue(errors.contains("Year is out of range."));
    }

    @Test
    public void testInvalidLicensePlate() {
        Vehicle v = new Vehicle(
            UUID.randomUUID(),
            "VIN123",
            "Honda",
            "Civic",
            2020,
            "",
            50000
        );

        List<String> errors = VehicleValidator.validate(v);
        assertEquals(1, errors.size());
        assertTrue(errors.contains("License plate is required."));
    }

    @Test
    public void testMultipleInvalidFields() {
        Vehicle v = new Vehicle(
            UUID.randomUUID(),
            "VIN123",
            "",
            "",
            1800,
            "",
            50000
        );

        List<String> errors = VehicleValidator.validate(v);
        assertEquals(4, errors.size());
        assertTrue(errors.contains("Make is required."));
        assertTrue(errors.contains("Model is required."));
        assertTrue(errors.contains("Year is out of range."));
        assertTrue(errors.contains("License plate is required."));
    }
}