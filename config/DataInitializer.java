
package com.defence.studentapp.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.defence.studentapp.entity.User;
import com.defence.studentapp.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createAdminUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String adminUsername = "admin";
            String adminEmail = "admin@studentapp.com";
            String adminPassword = "admin123";

            if (!userRepository.existsByUsername(adminUsername)) {

                User admin = new User();

                admin.setUsername(adminUsername);
                admin.setEmail(adminEmail);

                admin.setPassword(
                        passwordEncoder.encode(adminPassword)
                );

                admin.setRole("ADMIN");

                userRepository.save(admin);

                System.out.println(
                        "ADMIN user created successfully."
                );

            } else {

                System.out.println(
                        "ADMIN user already exists."
                );
            }
        };
    }
}

