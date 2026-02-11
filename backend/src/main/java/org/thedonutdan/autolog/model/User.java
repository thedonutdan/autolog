package org.thedonutdan.autolog.model;

import java.util.UUID;

/**
 * Represents a user
 */
public class User {
    private UUID userId;
    private String username;
    private String passwordHash;
    private boolean guest = false;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public boolean isGuest() { return guest; }
    public void setGuest(boolean guest) { this.guest = guest; }
}
