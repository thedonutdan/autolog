package org.thedonutdan.autolog.DAO.impl;

import org.thedonutdan.autolog.DAO.UserDAO;
import org.thedonutdan.autolog.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Profile;


/**
 * User data access object for Postgres configuration
 */
@Repository
@Profile("postgres")
public class PostgresUserDAO implements UserDAO{
    private final DataSource datasource;

    @Autowired
    public PostgresUserDAO(DataSource dataSource) {
        this.datasource = dataSource;
    }

    @Override
    public boolean insert(User user) {
        String query = """
                INSERT INTO users (user_id, username, password_hash, guest) VALUES (?, ?, ?, ?)
                """;
        
        try (Connection conn = datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setObject(1, user.getUserId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPasswordHash());
            stmt.setBoolean(4, user.isGuest());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public User findByUsername(String username) {
        String query = """
                SELECT * FROM users WHERE username = ?
                """;
        
        try (Connection conn = datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getObject("user_id", UUID.class));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setGuest(rs.getBoolean("guest"));

                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User findById(UUID userId) {
        String query = """
                SELECT * FROM users WHERE user_id = ?
                """;
        
        try (Connection conn = datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setObject(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getObject("user_id", UUID.class));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setGuest(rs.getBoolean("guest"));

                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean delete(UUID userId) {
        String query = """
                DELETE FROM users WHERE user_id = ?
                """;
        
        try (Connection conn = datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setObject(1, userId);
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
