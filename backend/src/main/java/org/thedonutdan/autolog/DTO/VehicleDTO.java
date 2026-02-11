package io.thedonutdan.vehiclemaintenance.DTO;

import io.thedonutdan.vehiclemaintenance.model.MaintenanceRecord;
import io.thedonutdan.vehiclemaintenance.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Represents a vehicle that is being monitored */
public class VehicleDTO {
    private UUID id;
    private String VIN;
    private String make;
    private String model;
    private int year;
    private String licensePlate;
    private int mileage;
    private List<MaintenanceRecord> maintenanceHistory;

    /** For JSON serialization/deserialization */
    public VehicleDTO() {

    }

    public VehicleDTO(String VIN, String make, String model, int year, String licensePlate, int mileage) {
        this.id = UUID.randomUUID();
        this.VIN = VIN;
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.mileage = mileage;
        this.maintenanceHistory = new ArrayList<>();
    }

    public VehicleDTO(String VIN, String make, String model, int year, String licensePlate, int mileage, List<MaintenanceRecord> maintenanceHistory) {
        this.id = UUID.randomUUID();
        this.VIN = VIN;
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.mileage = mileage;
        this.maintenanceHistory = maintenanceHistory;
    }

    public static VehicleDTO from(Vehicle v) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(v.getId());
        dto.setVIN(v.getVIN());
        dto.setMake(v.getMake());
        dto.setModel(v.getModel());
        dto.setYear(v.getYear());
        dto.setLicensePlate(v.getLicensePlate());
        dto.setMileage(v.getMileage());
        dto.setMaintenanceHistory(v.getMaintenanceHistory());
        return dto;
    }

    public UUID getId() {
        return id;
    }

    public String getVIN() {
        return VIN;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getMileage() {
        return mileage;
    }

    public List<MaintenanceRecord> getMaintenanceHistory() {
        return maintenanceHistory;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setMaintenanceHistory(List<MaintenanceRecord> records) {
        this.maintenanceHistory = records;
    }

    /**
     *  Adds a maintenance record to the vehicle, will update vehicle mileage if new 
     * @param record Maintenance record to be added to vehicle
    */
    public void addMaintenance(MaintenanceRecord record) {
        maintenanceHistory.add(record);
        if (record.getMileage() > mileage) {
            mileage = record.getMileage();
        }
    }

    public String toString() {
        return """
                Vehicle[
                id=%s,
                vin=%s,
                make=%s,
                model=%s,
                year=%d,
                licensePlate=%s
                mileage=%d
                ]
                """.formatted(id, VIN, make, model, year, licensePlate, mileage);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        VehicleDTO other = (VehicleDTO)obj;
        return Objects.equals(id, other.getId()); // UUID is statistically unique
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
