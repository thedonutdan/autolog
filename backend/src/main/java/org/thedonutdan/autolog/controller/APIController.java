package org.thedonutdan.autolog.controller;

import org.thedonutdan.autolog.DAO.UserDAO;
import org.thedonutdan.autolog.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class APIController {

    private final UserDAO userDAO;

    @Autowired
    public APIController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Returns login information to user
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Boolean>> me(Authentication auth) {
        UUID userId = (UUID) auth.getPrincipal();
        User user = userDAO.findById(userId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(
            Map.of("guest", user.isGuest())
        );
    }
}
