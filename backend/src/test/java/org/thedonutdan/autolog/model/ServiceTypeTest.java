package org.thedonutdan.autolog.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Period;

import java.io.IOException;

public class ServiceTypeTest {
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    
    /* Test constructor */
    @Test
    public void testCreateServiceType() {
        ServiceType stName = new ServiceType("Oil Change", null, null);
        ServiceType stExpiryMiles = new ServiceType("Tire Rotation", 5000, null);
        ServiceType stExpiryTime = new ServiceType("New Tires", null, Period.ofYears(5));
        ServiceType stExpiryMilesExpiryTime = new ServiceType("Air filter", 5000, Period.ofMonths(6));

        assertEquals("Oil Change", stName.getName());
        assertNull(stName.getDefaultExpiryMiles());
        assertNull(stName.getDefaultExpiryTime());

        assertEquals("Tire Rotation", stExpiryMiles.getName());
        assertEquals(5000, stExpiryMiles.getDefaultExpiryMiles());
        assertNull(stExpiryMiles.getDefaultExpiryTime());

        assertEquals("New Tires", stExpiryTime.getName());
        assertNull(stExpiryTime.getDefaultExpiryMiles());
        assertEquals(Period.ofYears(5), stExpiryTime.getDefaultExpiryTime());

        assertEquals("Air filter", stExpiryMilesExpiryTime.getName());
        assertEquals(5000, stExpiryMilesExpiryTime.getDefaultExpiryMiles());
        assertEquals(Period.ofMonths(6), stExpiryMilesExpiryTime.getDefaultExpiryTime());
    }

    /* Test equals function */
    @Test
    public void testEquality() {
        ServiceType a = new ServiceType("Oil Change", 5000, Period.ofMonths(6));
        ServiceType b = new ServiceType("Oil Change", 5000, Period.ofMonths(6));
        assertEquals(a, b);
    }

    @Test
    public void testToString() {
        ServiceType st = new ServiceType("Oil Change", 5000, Period.ofMonths(6));
        assertTrue(st.toString().contains("Oil Change"));
        assertTrue(st.toString().contains("5000"));
        assertTrue(st.toString().contains("6"));
    }

    @Test
    public void testSerializationDeserializationRoundTrip() throws IOException {
        ServiceType original = new ServiceType("Tires", 50000, Period.ofYears(5));
        String json = mapper.writeValueAsString(original);
        ServiceType deserialized = mapper.readValue(json, ServiceType.class);

        assertEquals(original.getName(), deserialized.getName());
        assertEquals(original.getDefaultExpiryMiles(), deserialized.getDefaultExpiryMiles());
        assertEquals(original.getDefaultExpiryTime(), deserialized.getDefaultExpiryTime());
    }
}