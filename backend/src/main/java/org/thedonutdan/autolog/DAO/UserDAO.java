package io.thedonutdan.vehiclemaintenance.DAO;

import java.util.UUID;

import io.thedonutdan.vehiclemaintenance.model.User;

/**
 * User data access interface for interacting with users stored in database
 */
public interface UserDAO {
    /**
     * Creates a new user in the database
     * @param user User object to store in the database
     * @return True on successful creation, false on failure
     */
    boolean insert(User user);
    /**
     * Retrieves a user object according to username
     * @param username Username of user to retrieve
     * @return User object corresponding to username or null
     */
    User findByUsername(String username);
    /**
     * Retrieves a user object according to user id
     * @param userId Id of user to be retrieved
     * @return User object corresponding to user id or null
     */
    User findById(UUID userId);
    /**
     * Removes a user from the database
     * @param userId Id of user to be removed
     * @return True on successful removal, false on failure
     */
    boolean delete(UUID userId);
}
