package org.thedonutdan.autolog.manager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.thedonutdan.autolog.model.Vehicle;
import org.thedonutdan.autolog.DAO.DummyVehicleDAO;
import org.thedonutdan.autolog.DAO.VehicleDAO;
import org.thedonutdan.autolog.model.MaintenanceRecord;
import org.thedonutdan.autolog.model.ServiceType;

import java.util.UUID;
import java.time.LocalDate;
import java.time.Period;

public class VehicleManagerTest {
    private VehicleManager manager;
    private VehicleDAO dao;
    
    @BeforeEach
    public void setup() {
        dao = new DummyVehicleDAO();
        manager = new VehicleManager(dao);
    }

    @Test
    public void testUnauthorizedGetVehicle() {
        UUID ownerId = UUID.randomUUID();
        UUID requesterId = UUID.randomUUID();
        
        Vehicle v = new Vehicle(ownerId, "VIN123", "Honda", "Civic", 2020, "ABC123", 50000);
        dao.insert(v);
        
        assertNull(manager.getVehicleById(v.getId(), requesterId));
    }

    @Test
    public void testUnauthorizedUpdateVehicle() {
        UUID ownerId = UUID.randomUUID();
        UUID requesterId = UUID.randomUUID();
        
        Vehicle v = new Vehicle(ownerId, "VIN123", "Honda", "Civic", 2020, "ABC123", 50000);
        dao.insert(v);
        
        assertFalse(manager.updateVehicle(requesterId, v));
    }

    @Test
    public void testUnauthorizedDeleteVehicle() {
        UUID ownerId = UUID.randomUUID();
        UUID requesterId = UUID.randomUUID();
        
        Vehicle v = new Vehicle(ownerId, "VIN123", "Honda", "Civic", 2020, "ABC123", 50000);
        dao.insert(v);
        
        assertFalse(manager.deleteVehicle(requesterId, v.getId()));
    }

    @Test
    public void testUnauthorizedAddMaintenance() {
        UUID ownerId = UUID.randomUUID();
        UUID requesterId = UUID.randomUUID();
        
        Vehicle v = new Vehicle(ownerId, "VIN123", "Honda", "Civic", 2020, "ABC123", 50000);
        dao.insert(v);
        
        MaintenanceRecord record = new MaintenanceRecord(
            LocalDate.now(), 
            new ServiceType("Oil Change", 5000, Period.ofMonths(6)),
            55000,
            null,
            null,
            "5W-30"
        );
        
        assertFalse(manager.addMaintenanceRecord(requesterId, v.getId(), record));
    }
}