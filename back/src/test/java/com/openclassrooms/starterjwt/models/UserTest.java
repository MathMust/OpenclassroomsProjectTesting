package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void builder_shouldCreateUserCorrectly() {
        LocalDateTime localDateTime = LocalDateTime.now();

        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(true)
                .createdAt(localDateTime)
                .updatedAt(localDateTime)
                .build();

        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password", user.getPassword());
        assertTrue(user.isAdmin());
        assertEquals(localDateTime, user.getCreatedAt());
        assertEquals(localDateTime, user.getUpdatedAt());
    }

    @Test
    void requiredArgsConstructor_shouldWork() {
        User user = new User(
                "test@example.com",
                "Doe",
                "John",
                "password",
                false
        );

        assertEquals("test@example.com", user.getEmail());
        assertEquals("Doe", user.getLastName());
        assertEquals("John", user.getFirstName());
        assertEquals("password", user.getPassword());
        assertFalse(user.isAdmin());
    }

    @Test
    void settersAndChainAccessors_shouldWork() {
        User user = new User()
                .setId(1L)
                .setEmail("test@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("password")
                .setAdmin(true)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        assertEquals("test@example.com", user.getEmail());
        assertTrue(user.isAdmin());
    }

    @Test
    void equalsAndHashCode_shouldBeBasedOnId() {
        User user1 = User.builder()
                .id(1L)
                .email("a@test.com")
                .firstName("A")
                .lastName("B")
                .password("pwd")
                .admin(false)
                .build();

        User user2 = User.builder()
                .id(1L)
                .email("other@test.com")
                .firstName("X")
                .lastName("Y")
                .password("other")
                .admin(true)
                .build();

        User user3 = User.builder()
                .id(2L)
                .email("a@test.com")
                .firstName("A")
                .lastName("B")
                .password("pwd")
                .admin(false)
                .build();

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1, user3);
        assertNotEquals(user1, null);
        assertNotEquals(user1, "string");
    }

    @Test
    void toString_shouldBeDifferentForDifferentUsers() {
        User user1 = User.builder()
                .id(1L)
                .email("a@test.com")
                .firstName("A")
                .lastName("B")
                .password("pwd")
                .admin(false)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("b@test.com")
                .firstName("C")
                .lastName("D")
                .password("pwd2")
                .admin(true)
                .build();

        assertNotEquals(user1.toString(), user2.toString());
    }

    @Test
    void requiredArgsConstructorShort_shouldThrowException_whenNullField() {
        // email non null
        assertThrows(NullPointerException.class, () -> new User(null, "Doe", "John", "password", true));

        // lastName non null
        assertThrows(NullPointerException.class, () -> new User("test@example.com", null, "John", "password", true));

        // firstName non null
        assertThrows(NullPointerException.class, () -> new User("test@example.com", "Doe", null, "password", true));

        // password non null
        assertThrows(NullPointerException.class, () -> new User("test@example.com", "Doe", "John", null, true));
    }

    @Test
    void requiredArgsConstructorLong_shouldThrowException_whenNullField() {
        // email non null
        assertThrows(NullPointerException.class, () -> new User(1L, null, "Doe", "John", "password", true, LocalDateTime.now(), LocalDateTime.now()));

        // lastName non null
        assertThrows(NullPointerException.class, () -> new User(1L, "test@example.com", null, "John", "password", true, LocalDateTime.now(), LocalDateTime.now()));

        // firstName non null
        assertThrows(NullPointerException.class, () -> new User(1L, "test@example.com", "Doe", null, "password", true, LocalDateTime.now(), LocalDateTime.now()));

        // password non null
        assertThrows(NullPointerException.class, () -> new User(1L, "test@example.com", "Doe", "John", null, true, LocalDateTime.now(), LocalDateTime.now()));
    }

    @Test
    void requiredSetter_shouldThrowException_whenNullField() {
        // email non null
        assertThrows(NullPointerException.class, () -> new User().setEmail(null));

        // lastName non null
        assertThrows(NullPointerException.class, () -> new User().setFirstName(null));

        // firstName non null
        assertThrows(NullPointerException.class, () -> new User().setLastName(null));

        // password non null
        assertThrows(NullPointerException.class, () -> new User().setPassword(null));

    }

    @Test
    void requiredBuider_shouldThrowException_whenNullField() {
        // email non null
        assertThrows(NullPointerException.class, () -> User.builder().id(1L).email(null).firstName("John").lastName("Doe").password("password").admin(true).build());

        // lastName non null
        assertThrows(NullPointerException.class, () -> User.builder().id(1L).email("test@example.com").firstName("John").lastName(null).password("password").admin(true).build());

        // firstName non null
        assertThrows(NullPointerException.class, () -> User.builder().id(1L).email("test@example.com").firstName(null).lastName("Doe").password("password").admin(true).build());

        // password non null
        assertThrows(NullPointerException.class, () -> User.builder().id(1L).email("test@example.com").firstName("John").lastName("Doe").password(null).admin(true).build());

    }



}
