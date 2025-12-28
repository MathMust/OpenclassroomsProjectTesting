package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {

    @Test
    void constructor_and_getters_shouldWorkCorrectly() {
        String token = "jwt-token";
        Long id = 1L;
        String username = "test@example.com";
        String firstName = "John";
        String lastName = "Doe";
        Boolean admin = true;

        JwtResponse response = new JwtResponse(
                token,
                id,
                username,
                firstName,
                lastName,
                admin
        );

        // Vérification constructeur
        assertEquals(token, response.getToken());
        assertEquals("Bearer", response.getType()); // valeur par défaut
        assertEquals(id, response.getId());
        assertEquals(username, response.getUsername());
        assertEquals(firstName, response.getFirstName());
        assertEquals(lastName, response.getLastName());
        assertEquals(admin, response.getAdmin());
    }

    @Test
    void setters_shouldUpdateValues() {
        JwtResponse response = new JwtResponse(
                "token",
                1L,
                "user",
                "first",
                "last",
                false
        );

        response.setToken("new-token");
        response.setType("Custom");
        response.setId(2L);
        response.setUsername("new-user");
        response.setFirstName("Jane");
        response.setLastName("Smith");
        response.setAdmin(true);

        assertEquals("new-token", response.getToken());
        assertEquals("Custom", response.getType());
        assertEquals(2L, response.getId());
        assertEquals("new-user", response.getUsername());
        assertEquals("Jane", response.getFirstName());
        assertEquals("Smith", response.getLastName());
        assertTrue(response.getAdmin());
    }
}
