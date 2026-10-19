
package com.defence.studentapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.defence.studentapp.entity.User;
import com.defence.studentapp.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void unauthenticatedUserCannotAccessStudents() throws Exception {

        mockMvc.perform(get("/students"))
                .andExpect(status().isForbidden());
    }

    @Test
    void swaggerEndpointIsAccessible() throws Exception {

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    void loginReturnsJwtToken() throws Exception {

        String username = "integrationtest_" + System.currentTimeMillis();
        String password = "Test@12345";
        String email = username + "@example.com";

        createTestUser(username, password, email);

        String loginRequest = """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(username, password);

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void authenticatedUserCanAccessStudents() throws Exception {

        String token = getJwtToken();

        mockMvc.perform(
                get("/students")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void authenticatedAdminCanCreateStudent() throws Exception {

        String token = getJwtToken();

        String email =
                "integrationstudent_"
                        + System.currentTimeMillis()
                        + "@example.com";

        String studentRequest = """
                {
                    "name": "Integration Test Student",
                    "department": "Computer Science",
                    "email": "%s"
                }
                """.formatted(email);

        mockMvc.perform(
                post("/students")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name")
                        .value("Integration Test Student"))
                .andExpect(jsonPath("$.department")
                        .value("Computer Science"))
                .andExpect(jsonPath("$.email")
                        .value(email))
                .andExpect(jsonPath("$.id")
                        .exists());
    }

    @Test
    void authenticatedAdminCanCreateReadUpdateAndDeleteStudent()
            throws Exception {

        String token = getJwtToken();

        String email =
                "crudstudent_"
                        + System.currentTimeMillis()
                        + "@example.com";

        String createRequest = """
                {
                    "name": "CRUD Test Student",
                    "department": "Computer Science",
                    "email": "%s"
                }
                """.formatted(email);

        MvcResult createResult = mockMvc.perform(
                post("/students")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name")
                        .value("CRUD Test Student"))
                .andExpect(jsonPath("$.email")
                        .value(email))
                .andReturn();

        String responseBody =
                createResult.getResponse().getContentAsString();

        Long studentId = extractId(responseBody);

        mockMvc.perform(
                get("/students/" + studentId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(studentId))
                .andExpect(jsonPath("$.name")
                        .value("CRUD Test Student"))
                .andExpect(jsonPath("$.email")
                        .value(email));

        String updateRequest = """
                {
                    "name": "Updated CRUD Student",
                    "department": "Software Engineering",
                    "email": "%s"
                }
                """.formatted(email);

        mockMvc.perform(
                put("/students/" + studentId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(studentId))
                .andExpect(jsonPath("$.name")
                        .value("Updated CRUD Student"))
                .andExpect(jsonPath("$.department")
                        .value("Software Engineering"))
                .andExpect(jsonPath("$.email")
                        .value(email));

        mockMvc.perform(
                delete("/students/" + studentId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(
                get("/students/" + studentId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    private String getJwtToken() throws Exception {

        String username =
                "integrationtest_" + System.currentTimeMillis();

        String password = "Test@12345";
        String email = username + "@example.com";

        createTestUser(username, password, email);

        String loginRequest = """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(username, password);

        MvcResult loginResult = mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody =
                loginResult.getResponse().getContentAsString();

        return extractToken(responseBody);
    }

    private void createTestUser(
            String username,
            String password,
            String email) {

        User user = new User();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(
                passwordEncoder.encode(password));

        user.setRole("ADMIN");

        userRepository.save(user);
    }

    private String extractToken(String responseBody) {

        String tokenField = "\"token\":\"";

        int start = responseBody.indexOf(tokenField);

        if (start == -1) {
            throw new IllegalStateException(
                    "JWT token was not found in login response: "
                            + responseBody);
        }

        start += tokenField.length();

        int end = responseBody.indexOf("\"", start);

        if (end == -1) {
            throw new IllegalStateException(
                    "Invalid JWT token response: "
                            + responseBody);
        }

        return responseBody.substring(start, end);
    }

    private Long extractId(String responseBody) {

        String idField = "\"id\":";

        int start = responseBody.indexOf(idField);

        if (start == -1) {
            throw new IllegalStateException(
                    "Student ID was not found in response: "
                            + responseBody);
        }

        start += idField.length();

        int end = start;

        while (end < responseBody.length()
                && Character.isDigit(responseBody.charAt(end))) {

            end++;
        }

        return Long.parseLong(
                responseBody.substring(start, end));
    }
}

