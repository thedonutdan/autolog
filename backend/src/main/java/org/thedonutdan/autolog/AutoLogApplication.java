package org.thedonutdan.autolog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

/**
 * Spring boot application to serve API
 */
@SpringBootApplication
public class AutoLogApplication {
    public static void main(String args[]) {
        SpringApplication.run(AutoLogApplication.class, args);
    }
}
