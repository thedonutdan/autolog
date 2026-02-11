package org.thedonutdan.autolog.validation;

import org.thedonutdan.autolog.model.MaintenanceRecord;

import java.util.List;
import java.util.ArrayList;

import java.time.LocalDate;

/**
 * Provides validation utilities for MaintenanceRecord objects
 */
public class MaintenanceRecordValidator {
    public static List<String> validate(MaintenanceRecord record) {
        List<String> errors = new ArrayList<>();

        if (record.getDate() == null) {
            errors.add("Maintenance Date is required");
        } else if (record.getDate().isAfter(LocalDate.now())) {
            errors.add("Date cannot be in the future");
        }
        if (record.getMileage() < 0) {
            errors.add("Mileage must not be negative");
        }
        if (record.getServiceType() == null) {
            errors.add("Service type is required");
        } else if (record.getServiceType().getName().isBlank()) {
            errors.add("Service type name required");
        }
        return errors;
    }
}
