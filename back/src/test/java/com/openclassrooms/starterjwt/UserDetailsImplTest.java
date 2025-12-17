package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    @Test
    void userDetailsImpl_fullCoverage() {
        Long id = 1L;
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(id)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(true)
                .build();

        // Vérifier getters
        assertEquals(id, user.getId());
        assertEquals("test@example.com", user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertTrue(user.getAdmin());

        // Vérifier méthodes UserDetails
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());

        // Vérifier getAuthorities()
        assertEquals(new HashSet<>(), user.getAuthorities());

        // Vérifier equals()
        UserDetailsImpl sameId = UserDetailsImpl.builder().id(id).build();
        UserDetailsImpl differentId = UserDetailsImpl.builder().id(2L).build();

        // this == o
        assertEquals(user, user);
        // même id
        assertEquals(user, sameId);
        // id différent
        assertNotEquals(user, differentId);
        // comparaison avec null et autre type
        assertNotEquals(user, null);
        assertNotEquals(user, "string");
    }
}
