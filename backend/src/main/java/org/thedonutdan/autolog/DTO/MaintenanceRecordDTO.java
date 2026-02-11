package org.thedonutdan.autolog.DTO;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

public class MaintenanceRecordDTO {
    private LocalDate date;
    private String serviceTypeName;
    private int mileage;
    private Integer expiryMiles;
    private Period expiryTime;
    private String notes;

    /** For JSON serialization/deserialization */
    public MaintenanceRecordDTO() {

    }
    
    public LocalDate getDate() {
        return date;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public int getMileage() {
        return mileage;
    }

    public Integer getExpiryMiles() {
        return expiryMiles;
    }

    public Period getExpiryTime() {
        return expiryTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setServiceType(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setExpiryMileage(Integer expiryMiles) {
        this.expiryMiles = expiryMiles;
    }

    public void setExpiryTime(Period expiryTime) {
        this.expiryTime = expiryTime;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String toString() {
        return """
                MaintenanceRecord[
                date=%s,
                serviceTypeName=%s,
                mileage=%d,
                expiryMileage=%d,
                expiryDate=%s,
                notes=%s]
                """.formatted(date.toString(), serviceTypeName, mileage, expiryMiles, expiryTime.toString(), notes);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MaintenanceRecordDTO other = (MaintenanceRecordDTO)obj;
        return Objects.equals(date, other.getDate()) &&
               Objects.equals(serviceTypeName, other.getServiceTypeName()) &&
               Objects.equals(mileage, other.getMileage()) &&
               Objects.equals(expiryMiles, other.getExpiryMiles()) &&
               Objects.equals(expiryTime, other.getExpiryTime()) &&
               Objects.equals(notes, other.getNotes());
    }
}
