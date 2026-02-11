package org.thedonutdan.autolog.manager;

import org.thedonutdan.autolog.DAO.VehicleDAO;
import org.thedonutdan.autolog.model.MaintenanceRecord;
import org.thedonutdan.autolog.model.Vehicle;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Manages validated requests from control and provides authorization for database access. Creates, reads, updates, and deletes vehicles.
 */
@Component
public class VehicleManager {
    private final VehicleDAO vehicleDAO;

    @Autowired
    public VehicleManager(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    /**
     * Adds a vehicle to the database
     * @param vehicle Vehicle to be added to database
     * @param userId Id of user associated with vehicle
     */
    public boolean addVehicle(Vehicle vehicle, UUID userId) {
        if (!vehicle.getUserId().equals(userId)) {
            return false;
        } else {
            return vehicleDAO.insert(vehicle);
        }
    }

    /**
     * Retrieves a vehicle from the database
     * @param vehicleId id of vehicle to be retrieved
     * @param userId id of associated user, must be validated
     * @return Vehicle object if it exists and user is authorized, null otherwise
     */
    public Vehicle getVehicleById(UUID vehicleId, UUID userId) {
        Vehicle v = vehicleDAO.findById(vehicleId);
        if (!v.getUserId().equals(userId)) {
            return null;
        }
        return v;
    }

    /**
     * Retrieves all vehicle associated with given user id
     * @param userId User id to retrieve vehicles for
     * @return List of vehicles associated with given user id
     */
    public List<Vehicle> getVehiclesByUserId(UUID userId) {
        return vehicleDAO.findByUserId(userId);
    }

    /**
     * Updates a vehicle in the database
     * @param userId User id associated with vehicle
     * @param vehicle Updated vehicle object
     * @return True if update is successful, null if update is unsuccessful or user is not authorized
     */
    public boolean updateVehicle(UUID userId, Vehicle vehicle) {
        Vehicle existing = vehicleDAO.findById(vehicle.getId());
        if (existing == null || !existing.getUserId().equals(userId)) {
            return false;
        }
        vehicle.setUserId(userId);
        return vehicleDAO.update(vehicle);
    }

    /**
     * Removes a vehicle from the database
     * @param userId Associated user id used for authorization
     * @param vehicleId Id of vehicle to be removed
     * @return True on successful removal, false on failure or if user is not authorized
     */
    public boolean deleteVehicle(UUID userId, UUID vehicleId) {
        Vehicle existing = vehicleDAO.findById(vehicleId);
        if (existing == null || !existing.getUserId().equals(userId)) {
            return false;
        }

        return vehicleDAO.delete(vehicleId);
    }

    /**
     * Adds a maintenance record to a vehicle in the database
     * @param userId Associated user id for authorization
     * @param vehicleId Id of vehicle to add maintenance record to
     * @param record Record object to be added to vehicle
     * @return True upon successful update, false on failure or if user is not authorized
     */
    public boolean addMaintenanceRecord(UUID userId, UUID vehicleId, MaintenanceRecord record) {
        Vehicle vehicle = vehicleDAO.findById(vehicleId);
        if (vehicle == null || !vehicle.getUserId().equals(userId)) {
            return false;
        }

        vehicle.addMaintenance(record);
        return vehicleDAO.update(vehicle);
    }
}
