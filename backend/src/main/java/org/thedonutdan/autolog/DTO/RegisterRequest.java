package org.thedonutdan.autolog.DTO;

/**
 * Data transfer object containing user registration information (username and password)
 */
public class RegisterRequest {
    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; } 
    public void setPassword(String password) { this.password = password; }
}
