package com.defence.studentapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.defence.studentapp.entity.User;
import com.defence.studentapp.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByUsername("admin")) {

                User admin = new User();

                admin.setUsername("admin");
                admin.setEmail("admin@studentapp.com");
                admin.setPassword(
                        passwordEncoder.encode("admin123")
                );
                admin.setRole("ADMIN");

                userRepository.save(admin);

                System.out.println(
                        "ADMIN user created successfully!"
                );
            }
        };
    }
}