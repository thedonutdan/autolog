package io.thedonutdan.vehiclemaintenance.model;

import java.time.Period;
import java.util.Objects;

/**
 * Represents a Service Type with optional default expiry miles and time
 */
public class ServiceType {
    private String name;
    private Integer defaultExpiryMiles;
    private Period defaultExpiryTime;

    /** Default constructor for JSON serialization/deserialization */
    public ServiceType() {

    }

    /** Full Constructor
     * 
     * @param name Name of service type (i.e. 'Oil Change', 'Tire Rotation', etc.)
     * @param defaultExpiryMiles The default amount of miles after which this service should be repeated.
     *  Can be left null to indicate no explicit expiry miles.
     * @param defaultExpiryTime The default amount of time after which this service should be repeated.
     *  Can be left null to indicate no explicit expiry time.
     */
    public ServiceType(String name, Integer defaultExpiryMiles, Period defaultExpiryTime) {
        this.name = name;
        this.defaultExpiryMiles = defaultExpiryMiles;
        this.defaultExpiryTime = defaultExpiryTime;
    }

    public String getName() {
        return name;
    }

    public Integer getDefaultExpiryMiles() {
        return defaultExpiryMiles;
    }

    public Period getDefaultExpiryTime() {
        return defaultExpiryTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultExpiryMiles(Integer defaultExpiryMiles) {
        this.defaultExpiryMiles = defaultExpiryMiles;
    }

    public void setDefaultExpiryTime(Period defaultExpiryTime) {
        this.defaultExpiryTime = defaultExpiryTime;
    }

    public String toString() {
        return """
                ServiceType[
                name=%s,
                defaultExpiryMiles=%d,
                defaultExpiryTime=%s
                ]
                """.formatted(name, defaultExpiryMiles, defaultExpiryTime.toString());
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ServiceType other = (ServiceType)obj;
        return Objects.equals(this.name, other.getName()) &&
               Objects.equals(this.defaultExpiryMiles, other.getDefaultExpiryMiles()) &&
               Objects.equals(this.defaultExpiryTime, other.getDefaultExpiryTime());
    }
}
