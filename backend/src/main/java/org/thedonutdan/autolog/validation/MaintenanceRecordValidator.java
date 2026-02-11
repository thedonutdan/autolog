package io.thedonutdan.vehiclemaintenance.validation;

import io.thedonutdan.vehiclemaintenance.model.MaintenanceRecord;

import java.util.List;
import java.util.ArrayList;

/**
 * Provides validation utilities for MaintenanceRecord objects
 */
public class MaintenanceRecordValidator {
    public static List<String> validate(MaintenanceRecord record) {
        List<String> errors = new ArrayList<>();

        if (record.getDate() == null) {
            errors.add("Maintenance Date is required");
        }
        if (record.getMileage() < 0) {
            errors.add("Mileage must not be negative");
        }
        if (record.getServiceType().getName() == null || record.getServiceType().getName().isBlank()) {
            errors.add("Service type name required");
        }
        return errors;
    }
}
