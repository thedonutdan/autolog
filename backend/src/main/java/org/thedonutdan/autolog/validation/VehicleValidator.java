package io.thedonutdan.vehiclemaintenance.validation;

import java.util.ArrayList;
import java.util.List;

import io.thedonutdan.vehiclemaintenance.model.Vehicle;

/**
 * Provides validation utilities for Vehicle objects
 */
public class VehicleValidator {
    public static List<String> validate(Vehicle vehicle) {
        List<String> errors = new ArrayList<>();

        
        if (vehicle.getMake() == null || vehicle.getMake().isBlank())
            errors.add("Make is required.");
        if (vehicle.getModel() == null || vehicle.getModel().isBlank())
            errors.add("Model is required.");
        if (vehicle.getYear() < 1886 || vehicle.getYear() > java.time.Year.now().getValue() + 1)
            errors.add("Year is out of range.");
        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().isBlank())
            errors.add("License plate is required.");

        return errors;
    }
}
