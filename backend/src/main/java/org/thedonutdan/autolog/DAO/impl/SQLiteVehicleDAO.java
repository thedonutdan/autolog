package io.thedonutdan.vehiclemaintenance.DAO.impl;

import io.thedonutdan.vehiclemaintenance.DAO.VehicleDAO;
import io.thedonutdan.vehiclemaintenance.model.MaintenanceRecord;
import io.thedonutdan.vehiclemaintenance.model.ServiceType;
import io.thedonutdan.vehiclemaintenance.model.Vehicle;

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

/**
 * Vehicle Data Access Object for SQLite configuration
 */
@Repository
public class SQLiteVehicleDAO implements VehicleDAO {
    private final Connection conn;

    @Autowired
    public SQLiteVehicleDAO(DataSource dataSource) throws SQLException {
        this.conn = dataSource.getConnection();
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
                date,
                service_type_name,
                service_type_default_expiry_miles,
                service_type_default_expiry_time,
                mileage,
                expiry_mileage,
                expiry_date,
                notes
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        
        try {
            conn.setAutoCommit(false);
    
            try (PreparedStatement stmt = conn.prepareStatement(vehiclesQuery)) {
                stmt.setString(1, vehicle.getId().toString());
                stmt.setString(2, vehicle.getUserId().toString());
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
                    stmt.setString(1, vehicle.getId().toString());
                    stmt.setString(2, record.getDate().toString());
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
                    stmt.setString(8, record.getExpiryDate() != null ? record.getExpiryDate().toString() : null);
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
        }
    }

    @Override
    public Vehicle findById(UUID id) {
        String query = """
                SELECT * FROM vehicles WHERE id = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id.toString());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(UUID.fromString(rs.getString("id")));
                vehicle.setUserId(UUID.fromString(rs.getString("user_id")));
                vehicle.setVIN(rs.getString("vin"));
                vehicle.setMake(rs.getString("make"));
                vehicle.setModel(rs.getString("model"));
                vehicle.setYear(rs.getInt("year"));
                vehicle.setLicensePlate(rs.getString("license_plate"));
                vehicle.setMileage(rs.getInt("mileage"));
                vehicle.setMaintenanceHistory(getMaintenanceRecords(id));

                return vehicle;
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

        try (PreparedStatement stmt = conn.prepareStatement(vehiclesQuery)) {
            stmt.setString(1, userId.toString());

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(UUID.fromString(rs.getString("id")));
                vehicle.setUserId(UUID.fromString(rs.getString("user_id")));
                vehicle.setVIN(rs.getString("vin"));
                vehicle.setMake(rs.getString("make"));
                vehicle.setModel(rs.getString("model"));
                vehicle.setYear(rs.getInt("year"));
                vehicle.setLicensePlate(rs.getString("license_plate"));
                vehicle.setMileage(rs.getInt("mileage"));
                vehicle.setMaintenanceHistory(getMaintenanceRecords(vehicle.getId()));

                vehicles.add(vehicle);
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

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(deleteMaintenanceRecordsQuery)) {
                stmt.setString(1, vehicle.getId().toString());
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(deleteVehiclesQuery)) {
                stmt.setString(1, vehicle.getId().toString());

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
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(maintenanceRecordsQuery)) {
                stmt.setString(1, id.toString());

                stmt.executeUpdate();

            }

            try (PreparedStatement stmt = conn.prepareStatement(vehiclesQuery)) {
                stmt.setString(1, id.toString());
    
                stmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
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
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id.toString());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MaintenanceRecord record = new MaintenanceRecord();
                ServiceType st = new ServiceType();
                st.setName(rs.getString("service_type_name"));
                st.setDefaultExpiryMiles(getNullableInt(rs, "service_type_default_expiry_miles"));
                st.setDefaultExpiryTime(getNullablePeriod(rs, "service_type_default_expiry_time"));

                record.setDate(LocalDate.parse(rs.getString("date")));
                record.setServiceType(st);
                record.setMileage(rs.getInt("mileage"));
                record.setExpiryMileage(getNullableInt(rs, "expiry_mileage"));
                record.setExpiryDate(getNullableDate(rs, "expiry_date"));
                record.setNotes(rs.getString("notes"));

                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }

    private Integer getNullableInt(ResultSet rs, String column) throws SQLException {
        int val = rs.getInt(column);
        return rs.wasNull() ? null : val;
    }

    private Period getNullablePeriod(ResultSet rs, String column) throws SQLException {
        String raw = rs.getString(column);
        return (raw != null) ? Period.parse(raw) : null;
    }

    private LocalDate getNullableDate(ResultSet rs, String column) throws SQLException {
        String raw = rs.getString(column);
        return (raw != null) ? LocalDate.parse(raw) : null;
    }
}