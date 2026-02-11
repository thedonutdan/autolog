package org.thedonutdan.autolog.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

import org.thedonutdan.autolog.DTO.MaintenanceRecordDTO;

/**
 * Represents a record of a specific maintenance performed on a vehicle
 */
public class MaintenanceRecord {
    private LocalDate date;
    private ServiceType serviceType;
    private int mileage;
    private Integer expiryMileage;
    private LocalDate expiryDate;
    private String notes;

    /** For JSON serialization/deserialization */
    public MaintenanceRecord() {

    }
    /** Full Constructor
     * @param date The date the maintenance took place
     * @param serviceType The type of service performed
     * @param mileage The mileage of the vehicle at time of maintenance
     * @param expiryMiles The number of miles after the maintenance at which it should be repeated 
     * (e.g. 5000 means the maintenance is valid for 5000 miles). Can be null to indicate the record
     * should use the default values provided in ServiceType, which can also be null to indicate
     * no explicit expiry milage.
     * @param expiryTime The amount of time after the maintenance at which it should be repeated
     * (e.g. 6 months, 1 year, etc). Can be null to indicate the record should use the
     * default values provided in ServiceType, which can also be null to indicate no explicit
     * expiry time.
     * @param notes Any notes associate with the maintenance.
     */
    public MaintenanceRecord(LocalDate date, ServiceType serviceType, int mileage, Integer expiryMiles, Period expiryTime, String notes) {
        this.date = date;
        this.serviceType = serviceType;
        this.mileage = mileage;
        this.notes = notes;
        if (expiryMiles == null) {
            if (serviceType.getDefaultExpiryMiles() != null) {
                this.expiryMileage = mileage + serviceType.getDefaultExpiryMiles();
            }
        } else {
            this.expiryMileage = mileage + expiryMiles;
        }
        if (expiryTime == null) {
            if (serviceType.getDefaultExpiryTime() != null) {
                this.expiryDate = date.plus(serviceType.getDefaultExpiryTime());
            }
        } else {
            this.expiryDate = date.plus(expiryTime);
        }
    }

    public static MaintenanceRecord from(MaintenanceRecordDTO dto) {
        ServiceType st = new ServiceType(dto.getServiceTypeName(), dto.getExpiryMiles(), dto.getExpiryTime());
        return new MaintenanceRecord(
            dto.getDate(),
            st,
            dto.getMileage(),
            dto.getExpiryMiles(),
            dto.getExpiryTime(),
            dto.getNotes());
    }

    public LocalDate getDate() {
        return date;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public int getMileage() {
        return mileage;
    }

    public Integer getExpiryMileage() {
        return expiryMileage;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void computeAndSetExpiryMileage(Integer expiryMiles) {
        this.expiryMileage = mileage + expiryMiles;
    }

    public void setExpiryMileage(Integer expiryMileage) {
        this.expiryMileage = expiryMileage;
    }

    public void computeAndSetExpiryDate(Period expiryTime) {
        this.expiryDate = this.date.plus(expiryTime);
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String toString() {
        return """
                MaintenanceRecord[
                date=%s,
                serviceType=%s,
                mileage=%d,
                expiryMileage=%d,
                expiryDate=%s,
                notes=%s]
                """.formatted(date.toString(), serviceType.toString(), mileage, expiryMileage, expiryDate.toString(), notes);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MaintenanceRecord other = (MaintenanceRecord)obj;
        return Objects.equals(date, other.getDate()) &&
               Objects.equals(serviceType, other.getServiceType()) &&
               Objects.equals(mileage, other.getMileage()) &&
               Objects.equals(expiryMileage, other.getExpiryMileage()) &&
               Objects.equals(expiryDate, other.getExpiryDate()) &&
               Objects.equals(notes, other.getNotes());
    }
}
