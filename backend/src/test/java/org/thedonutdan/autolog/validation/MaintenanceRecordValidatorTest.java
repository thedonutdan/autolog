package org.thedonutdan.autolog.validation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import org.thedonutdan.autolog.model.MaintenanceRecord;
import org.thedonutdan.autolog.model.ServiceType;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class MaintenanceRecordValidatorTest {

    @Test
    public void testValidMaintenanceRecord() {
        ServiceType st = new ServiceType("Oil Change", 5000, Period.ofMonths(6));
        MaintenanceRecord record = new MaintenanceRecord(
            LocalDate.now(),
            st,
            50000,
            null,
            null,
            "5W-30"
        );
        
        List<String> errors = MaintenanceRecordValidator.validate(record);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testInvalidDate() {
        ServiceType st = new ServiceType("Oil Change", 5000, Period.ofMonths(6));
        MaintenanceRecord record = new MaintenanceRecord(
            null,
            st,
            50000,
            null,
            null,
            "5W-30"
        );
        
        List<String> errors = MaintenanceRecordValidator.validate(record);
        assertEquals(1, errors.size());
        assertTrue(errors.contains("Maintenance Date is required"));
    }

    @Test
    public void testFutureDate() {
        ServiceType st = new ServiceType("Oil Change", 5000, Period.ofMonths(6));
        MaintenanceRecord record = new MaintenanceRecord(
            LocalDate.now().plusDays(1),
            st,
            50000,
            null,
            null,
            "5W-30"
        );
        
        List<String> errors = MaintenanceRecordValidator.validate(record);
        assertEquals(1, errors.size());
        assertTrue(errors.contains("Date cannot be in the future"));
    }

    @Test
    public void testInvalidMileage() {
        ServiceType st = new ServiceType("Oil Change", 5000, Period.ofMonths(6));
        MaintenanceRecord record = new MaintenanceRecord(
            LocalDate.now(),
            st,
            -1,
            null,
            null,
            "5W-30"
        );
        
        List<String> errors = MaintenanceRecordValidator.validate(record);
        assertEquals(1, errors.size());
        assertTrue(errors.contains("Mileage must not be negative"));
    }

    @Test
    public void testInvalidServiceType() {
        MaintenanceRecord record = new MaintenanceRecord(
            LocalDate.now(),
            null,
            50000,
            null,
            null,
            "5W-30"
        );
        
        List<String> errors = MaintenanceRecordValidator.validate(record);
        assertEquals(1, errors.size());
        assertTrue(errors.contains("Service type is required"));
    }
}