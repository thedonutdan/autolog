package io.thedonutdan.vehiclemaintenance.DAO;

import io.thedonutdan.vehiclemaintenance.model.Vehicle;

import java.util.List;
import java.util.UUID;

/**
 * Vehicle data access interface for interacting with vehicles in database
 */
public interface VehicleDAO {    
    /** Adds a vehicle to the database
     * @param vehicle Vehicle to be added to the database
     * @return True if insertion is successful, false on failure
     */
    public boolean insert(Vehicle vehicle);
    /**
     * Retrieves a vehicle by its id
     * @param id id of vehicle to retrieve
     * @return vehicle corresponding to id or null if not found
     */
    public Vehicle findById(UUID id);
    /**
     * Retrieves all vehicles related to a specific user id
     * @param id User id to find associated vehicles
     * @return List of vehicles associated with given user id
     */
    public List<Vehicle> findByUserId(UUID id);
    /**
     * Updates a vehicle in the database
     * @param vehicle Updated vehicle
     * @return True on successful update, false on failure
     */
    public boolean update(Vehicle vehicle);
    /**
     * Removes a vehicle from the database
     * @param id Id of vehicle to remove
     * @return True on successful removal, false on failure
     */
    public boolean delete(UUID id);
}
