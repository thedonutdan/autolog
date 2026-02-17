package org.thedonutdan.autolog.DAO.impl;

import org.thedonutdan.autolog.DAO.VehicleDAO;
import org.thedonutdan.autolog.model.MaintenanceRecord;
import org.thedonutdan.autolog.model.ServiceType;
import org.thedonutdan.autolog.model.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Profile;

/**
 * Vehicle Data Access Object for Postgres configuration
 */
@Repository
@Profile("postgres")
public class PostgresVehicleDAO implements VehicleDAO {
    private final DataSource datasource;

    @Autowired
    public PostgresVehicleDAO(DataSource dataSource) {
        this.datasource = dataSource;
    }

    @Override
    public boolean insert(Vehicle vehicle) {
        String vehiclesQuery = """
            INSERT INTO vehicles (
                id,
                user_id,
                vin,
                make,
                model,
                year,
                license_plate,
                mileage
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        
        String maintenanceRecordsQuery = """
                INSERT INTO maintenance_records (
                vehicle_id,
                service_date,
                service_type_name,
                service_type_default_expiry_miles,
                service_type_default_expiry_time,
                mileage,
                expiry_mileage,
                expiry_date,
                notes
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        Connection conn = null;

        try {
            conn = datasource.getConnection();
            conn.setAutoCommit(false);
    
            try (PreparedStatement stmt = conn.prepareStatement(vehiclesQuery)) {
                stmt.setObject(1, vehicle.getId());
                stmt.setObject(2, vehicle.getUserId());
                stmt.setString(3, vehicle.getVIN());
                stmt.setString(4, vehicle.getMake());
                stmt.setString(5, vehicle.getModel());
                stmt.setInt(6, vehicle.getYear());
                stmt.setString(7, vehicle.getLicensePlate());
                stmt.setInt(8, vehicle.getMileage());
                
                executeStrictUpdate(stmt, 1);
            }
    
            for (MaintenanceRecord record : vehicle.getMaintenanceHistory()) {
                try (PreparedStatement stmt = conn.prepareStatement(maintenanceRecordsQuery)) {
                    ServiceType st = record.getServiceType();
                    stmt.setObject(1, vehicle.getId());
                    stmt.setObject(2, record.getDate());
                    stmt.setString(3, st.getName());
                    if (st.getDefaultExpiryMiles() != null) {
                        stmt.setInt(4, st.getDefaultExpiryMiles());
                    } else {
                        stmt.setNull(4, java.sql.Types.INTEGER);
                    }
                    stmt.setString(5, st.getDefaultExpiryTime() != null ? st.getDefaultExpiryTime().toString() : null);
                    stmt.setInt(6, record.getMileage());
                    if (record.getExpiryMileage() != null) {
                        stmt.setInt(7, record.getExpiryMileage());
                    } else {
                        stmt.setNull(7, java.sql.Types.INTEGER);
                    }
                    stmt.setObject(8, record.getExpiryDate() != null ? record.getExpiryDate() : null);
                    stmt.setString(9, record.getNotes());
                    
                    executeStrictUpdate(stmt, 1);
                }
            }
    
            conn.commit();
            return true;
    
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    @Override
    public Vehicle findById(UUID id) {
        String query = """
                SELECT * FROM vehicles WHERE id = ?
                """;

        try (Connection conn = datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setId(rs.getObject("id", UUID.class));
                    vehicle.setUserId(rs.getObject("user_id", UUID.class));
                    vehicle.setVIN(rs.getString("vin"));
                    vehicle.setMake(rs.getString("make"));
                    vehicle.setModel(rs.getString("model"));
                    vehicle.setYear(rs.getInt("year"));
                    vehicle.setLicensePlate(rs.getString("license_plate"));
                    vehicle.setMileage(rs.getInt("mileage"));
                    vehicle.setMaintenanceHistory(getMaintenanceRecords(id));

                    return vehicle;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Vehicle> findByUserId(UUID userId) {
        String vehiclesQuery = """
                SELECT * FROM vehicles WHERE user_id = ?
                """;
        List<Vehicle> vehicles = new ArrayList<>();
        
        try (Connection conn = datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(vehiclesQuery);) {
            
            stmt.setObject(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {

                while(rs.next()) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setId(rs.getObject("id", UUID.class));
                    vehicle.setUserId(rs.getObject("user_id", UUID.class));
                    vehicle.setVIN(rs.getString("vin"));
                    vehicle.setMake(rs.getString("make"));
                    vehicle.setModel(rs.getString("model"));
                    vehicle.setYear(rs.getInt("year"));
                    vehicle.setLicensePlate(rs.getString("license_plate"));
                    vehicle.setMileage(rs.getInt("mileage"));
                    vehicle.setMaintenanceHistory(getMaintenanceRecords(vehicle.getId()));

                    vehicles.add(vehicle);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    @Override
    public boolean update(Vehicle vehicle) {
        String deleteVehiclesQuery = """
            DELETE FROM vehicles WHERE id = ?
            """;
        String deleteMaintenanceRecordsQuery = """
            DELETE FROM maintenance_records WHERE vehicle_id = ?
            """;
        Connection conn = null;

        try {
            conn = datasource.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(deleteMaintenanceRecordsQuery)) {
                stmt.setObject(1, vehicle.getId());
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(deleteVehiclesQuery)) {
                stmt.setObject(1, vehicle.getId());

                executeStrictUpdate(stmt, 1);
            }

            boolean insertSuccess = insert(vehicle);
            if (!insertSuccess) {
                throw new SQLException("Error re-inserting vehicle");
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollBackEx) {
                rollBackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }
    }

    @Override
    public boolean delete(UUID id) {
        String vehiclesQuery = """
                DELETE FROM vehicles WHERE id = ?
                """;
        String maintenanceRecordsQuery = """
                DELETE FROM maintenance_records WHERE vehicle_id = ?
                """;
        Connection conn = null;

        try {
            conn = datasource.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(maintenanceRecordsQuery)) {
                stmt.setObject(1, id);

                stmt.executeUpdate();

            }

            try (PreparedStatement stmt = conn.prepareStatement(vehiclesQuery)) {
                stmt.setObject(1, id);
    
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollBackEx) {
                rollBackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignore) {}
            }
        }

        return false;
    }

    private void executeStrictUpdate(PreparedStatement stmt, int expectedRows) throws SQLException {
        int affected = stmt.executeUpdate();
        if (affected != expectedRows) {
            throw new SQLException(String.format("Expected %d rows affected, got %d", expectedRows, affected));
        }
    }

    private List<MaintenanceRecord> getMaintenanceRecords(UUID id) {
        List<MaintenanceRecord> records = new ArrayList<>();
        String query = """
                SELECT * FROM maintenance_records WHERE vehicle_id = ?
                """;
        

        try (Connection conn = datasource.getConnection()) {
            
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setObject(1, id);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    MaintenanceRecord record = new MaintenanceRecord();
                    ServiceType st = new ServiceType();
                    st.setName(rs.getString("service_type_name"));
                    st.setDefaultExpiryMiles(rs.getObject("service_type_default_expiry_miles", Integer.class));
                    st.setDefaultExpiryTime(getNullablePeriod(rs, "service_type_default_expiry_time"));

                    record.setDate(rs.getObject("service_date", LocalDate.class));
                    record.setServiceType(st);
                    record.setMileage(rs.getInt("mileage"));
                    record.setExpiryMileage(rs.getObject("expiry_mileage", Integer.class));
                    record.setExpiryDate(rs.getObject("expiry_date", LocalDate.class));
                    record.setNotes(rs.getString("notes"));

                    records.add(record);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 

        return records;
    }

    private Period getNullablePeriod(ResultSet rs, String column) throws SQLException {
        String raw = rs.getString(column);
        return (raw != null) ? Period.parse(raw) : null;
    }
}