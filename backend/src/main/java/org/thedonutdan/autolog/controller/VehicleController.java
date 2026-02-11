package org.thedonutdan.autolog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.thedonutdan.autolog.DTO.VehicleDTO;
import org.thedonutdan.autolog.DTO.MaintenanceRecordDTO;
import org.thedonutdan.autolog.manager.VehicleManager;
import org.thedonutdan.autolog.model.MaintenanceRecord;
import org.thedonutdan.autolog.model.Vehicle;
import org.thedonutdan.autolog.validation.MaintenanceRecordValidator;
import org.thedonutdan.autolog.validation.VehicleValidator;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller for vehicle-related endpoints. Passes validated requests to VehicleManager
 */
@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleManager vehicleManager;

    @Autowired
    public VehicleController(VehicleManager vehicleManager) {
        this.vehicleManager = vehicleManager;
    }

    /**
     * Creates a vehicle in the database
     * @param dto the vehicle dto to add to the database
     * @param auth Authentication principle
     * @return HTTP 201 if vehicle is created successfully, 400 if vehicle object is malformed
     */
    @PostMapping
    public ResponseEntity<String> createVehicle(@RequestBody VehicleDTO dto, Authentication auth) {
        UUID userId = (UUID) auth.getPrincipal();
        Vehicle v = Vehicle.from(dto, userId);
        List<String> errors = VehicleValidator.validate(v);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(String.join("\n", errors));
        }

        v.setUserId(userId);
        UUID vehicleId = UUID.randomUUID();
        v.setId(vehicleId);
        boolean success = vehicleManager.addVehicle(v, userId);
        if (success){
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("X-Vehicle-Id", vehicleId.toString())
                .body("Vehicle created ");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vehicle not authorized");
        }
    }

    /**
     * Retrieves a vehicle from the database by vehicle id
     * @param vehicleId id of vehicle to retrieve
     * @param auth Authentication principle
     * @return HTTP 404 if vehicle does not exist, vehicle DTO and HTTP 200 if vehicle is found
     */
    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleDTO> getVehicle(@PathVariable UUID vehicleId, Authentication auth) {
        UUID userId = (UUID) auth.getPrincipal();
        Vehicle v = vehicleManager.getVehicleById(vehicleId, userId);
        if (v == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(VehicleDTO.from(v));
    }

    /**
     * Retrieves all vehicles associated with a specific user id
     * @param auth Authentication principle
     * @return HTTP 200 and list of vehicle DTOs associated with user id
     */
    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getVehiclesByUserId(Authentication auth) {
        UUID userId = (UUID) auth.getPrincipal();
        List<Vehicle> vehicles = vehicleManager.getVehiclesByUserId(userId);
        return ResponseEntity.ok(vehicles.stream()
                                        .map(VehicleDTO::from)
                                        .collect(Collectors.toList()));
    }

    /**
     * Updates a vehicle in the database
     * @param vehicleId id of vehicle to update
     * @param auth Authentication principle
     * @param dto updated vehicle DTO
     * @return HTTP 400 if vehicle is malformed or vehicle id in request does not match vehicle object, HTTP 403 if vehicle does not exist or is not authorized
     * for that user. HTTP 200 if update is successful.
     */
    @PutMapping("/{vehicleId}")
    public ResponseEntity<String> updateVehicle(
            @PathVariable UUID vehicleId,
            Authentication auth,
            @RequestBody VehicleDTO dto
        ) {
        if (!vehicleId.equals(dto.getId())) {
            return ResponseEntity.badRequest().body("Vehicle ID mismatch");
        }

        UUID userId = (UUID) auth.getPrincipal();

        Vehicle v = Vehicle.from(dto, userId);

        List<String> errors = VehicleValidator.validate(v);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(String.join("\n", errors));
        }

        boolean success = vehicleManager.updateVehicle(userId, v);
        if (!success) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vehicle not found or not authorized");
        }

        return ResponseEntity.ok("Vehicle updated");
    }
    
    /**
     * Adds a maintenance record to the vehicle with the corresponding ID
     * @param vehicleId ID of vehicle to add to
     * @param auth Authentication principle
     * @param dto Data transfer object for new Maintenance Record
     * @return
     */
    @PutMapping("/{vehicleId}/records")
    public ResponseEntity<MaintenanceRecord> addMaintenanceRecord(
        @PathVariable UUID vehicleId,
        Authentication auth,
        @RequestBody MaintenanceRecordDTO dto
    ) {
        UUID userId = (UUID) auth.getPrincipal();
        MaintenanceRecord record = MaintenanceRecord.from(dto);
        boolean success = vehicleManager.addMaintenanceRecord(userId, vehicleId, record);
        if (!success) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Database failure or vehicle is not authorized
        } else {
            return ResponseEntity.ok().body(record); 
        }
    }

    /**
     * Removes a vehicle from the database
     * @param vehicleId id of vehicle to be removed
     * @param auth Authentication principle
     * @return HTTP 403 if vehicle is not found or user is not authorized. HTTP 200 if removal is successful.
     */
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<String> deleteVehicle(
            @PathVariable UUID vehicleId,
            Authentication auth
        ) {
        UUID userId = (UUID) auth.getPrincipal();
        boolean success = vehicleManager.deleteVehicle(userId, vehicleId);
        if (!success) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vehicle not found or not authorized");
        }

        return ResponseEntity.ok("Vehicle deleted");
    }

    /**
     * Adds a maintenance record to a vehicle
     * @param vehicleId id of vehicle to be updated
     * @param auth Authentication principle
     * @param record record to be added to vehicle
     * @return HTTP 400 if record is malformed, HTTP 403 if vehicle is not found or user is not authorized. HTTP 200 if successful.
     */
    @PostMapping("/{vehicleId}/maintenance")
    public ResponseEntity<String> addMaintenanceRecord(
            @PathVariable UUID vehicleId,
            Authentication auth,
            @RequestBody MaintenanceRecord record
        ) {
        List<String> errors = MaintenanceRecordValidator.validate(record);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(String.join("\n", errors));
        }
        
        UUID userId = (UUID) auth.getPrincipal();
        boolean success = vehicleManager.addMaintenanceRecord(userId, vehicleId, record);
        if (!success) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vehicle not found or not authorized");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Maintenance record added");
    }
    
}
