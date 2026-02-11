package org.thedonutdan.autolog.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.time.Period;

import java.io.IOException;

public class MaintenanceRecordTest {
    private final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    @Test
    public void testCreateMaintenanceRecordAllValues() {
        LocalDate date = LocalDate.of(2025, 7, 1);
        ServiceType st = new ServiceType("Oil Change", null, null);
        MaintenanceRecord record = new MaintenanceRecord(date, st, 10000, 5000, Period.ofMonths(6), "5W-30");

        assertEquals(date, record.getDate());
        assertEquals(st, record.getServiceType());
        assertEquals(10000, record.getMileage());
        assertEquals(15000, record.getExpiryMileage());
        assertEquals(date.plus(Period.ofMonths(6)), record.getExpiryDate());
        assertEquals("5W-30", record.getNotes());
    }

    @Test
    public void testCreateMaintenanceRecordNullMileage() {
        LocalDate date = LocalDate.of(2025, 7, 1);
        ServiceType st = new ServiceType("Oil Change", 5000, null);
        MaintenanceRecord record = new MaintenanceRecord(date, st, 10000, null, Period.ofMonths(6), "5W-30");

        assertEquals(date, record.getDate());
        assertEquals(st, record.getServiceType());
        assertEquals(10000, record.getMileage());
        assertEquals(15000, record.getExpiryMileage());
        assertEquals(date.plus(Period.ofMonths(6)), record.getExpiryDate());
        assertEquals("5W-30", record.getNotes());
    }

    @Test
    public void testCreateMaintenanceRecordNullDate() {
        LocalDate date = LocalDate.of(2025, 7, 1);
        ServiceType st = new ServiceType("Oil Change", null, Period.ofMonths(6));
        MaintenanceRecord record = new MaintenanceRecord(date, st, 10000, 5000, null, "5W-30");

        assertEquals(date, record.getDate());
        assertEquals(st, record.getServiceType());
        assertEquals(10000, record.getMileage());
        assertEquals(15000, record.getExpiryMileage());
        assertEquals(date.plus(Period.ofMonths(6)), record.getExpiryDate());
        assertEquals("5W-30", record.getNotes());
    }

    @Test
    public void testCreateMaintenanceRecordNullBoth() {
        LocalDate date = LocalDate.of(2025, 7, 1);
        ServiceType st = new ServiceType("Oil Change", 5000, Period.ofMonths(6));
        MaintenanceRecord record = new MaintenanceRecord(date, st, 10000, null, null, "5W-30");
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        assertEquals(date, record.getDate());
        assertEquals(st, record.getServiceType());
        assertEquals(10000, record.getMileage());
        assertEquals(15000, record.getExpiryMileage());
        assertEquals(date.plus(Period.ofMonths(6)), record.getExpiryDate());
        assertEquals("5W-30", record.getNotes());
    }

    @Test
    public void testSerializationDeserializationRoundTrip() throws IOException {
        ServiceType st = new ServiceType("Oil Change", 5000, Period.ofMonths(6));
        MaintenanceRecord original = new MaintenanceRecord(LocalDate.of(2025, 7, 1), st, 10000, null, null, "5W-30");

        String json = mapper.writeValueAsString(original);
        MaintenanceRecord deserialized = mapper.readValue(json, MaintenanceRecord.class);

        assertEquals(original, deserialized);
    }
}