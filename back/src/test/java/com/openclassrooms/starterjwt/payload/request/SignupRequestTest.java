package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {

    @Test
    void shouldSetAndGetAllFields() {
        SignupRequest request = new SignupRequest();

        request.setEmail("test@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password123");

        assertEquals("test@example.com", request.getEmail());
        assertEquals("John", request.getFirstName());
        assertEquals("Doe", request.getLastName());
        assertEquals("password123", request.getPassword());
    }

    @Test
    void equalsAndHashCode_shouldWork() {
        SignupRequest r1 = new SignupRequest();
        r1.setEmail("test@example.com");
        r1.setFirstName("John");
        r1.setLastName("Doe");
        r1.setPassword("password123");

        SignupRequest r2 = new SignupRequest();
        r2.setEmail("test@example.com");
        r2.setFirstName("John");
        r2.setLastName("Doe");
        r2.setPassword("password123");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void toString_shouldNotBeNull() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@example.com");

        assertNotNull(request.toString());
        assertTrue(request.toString().contains("email"));
    }

    @Test
    void testEqualsAndHashCode() {
        SignupRequest r1 = new SignupRequest();
        r1.setEmail("a@example.com");
        r1.setFirstName("John");
        r1.setLastName("Doe");
        r1.setPassword("password");

        SignupRequest r2 = new SignupRequest();
        r2.setEmail("a@example.com");
        r2.setFirstName("John");
        r2.setLastName("Doe");
        r2.setPassword("password");

        SignupRequest r3 = new SignupRequest();
        r3.setEmail("b@example.com");
        r3.setFirstName("Jane");
        r3.setLastName("Smith");
        r3.setPassword("123456");

        // même contenu
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());

        // même instance
        assertEquals(r1, r1);

        // null
        assertNotEquals(r1, null);

        // autre classe
        assertNotEquals(r1, "string");

        // contenu différent
        assertNotEquals(r1, r3);
    }
}
