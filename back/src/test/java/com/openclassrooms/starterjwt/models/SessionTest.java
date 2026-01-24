package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    @Test
    void shouldBuildSessionWithBuilder() {
        Date date = new Date();
        LocalDateTime localDateTime = LocalDateTime.now();

        Teacher teacher = new Teacher(1L,"LastName", "FirstName", localDateTime, localDateTime);

        Session session = Session.builder()
                .id(1L)
                .name("Spring Boot")
                .description("Formation Spring Boot")
                .date(date)
                .users(Arrays.asList())
                .teacher(teacher)
                .createdAt(localDateTime)
                .updatedAt(localDateTime)
                .build();

        assertEquals(1L, session.getId());
        assertEquals("Spring Boot", session.getName());
        assertEquals("Formation Spring Boot", session.getDescription());
        assertEquals(date, session.getDate());
        assertNotNull(session.getUsers());
        assertEquals("LastName", session.getTeacher().getLastName());
        assertEquals(localDateTime, session.getCreatedAt());
        assertEquals(localDateTime, session.getUpdatedAt());
    }

    @Test
    void shouldSupportChainedAccessors() {
        Session session = new Session()
                .setId(1L)
                .setName("Session name")
                .setDescription("Description")
                .setDate(new Date());

        assertEquals(1L, session.getId());
        assertEquals("Session name", session.getName());
    }

    @Test
    void equals_shouldBeBasedOnIdOnly() {
        Date date = new Date();
        LocalDateTime localDateTime = LocalDateTime.now();

        Teacher teacher = new Teacher(1L,"LastName", "FirstName", localDateTime, localDateTime);

        Session s1 = Session.builder()
                .id(1L)
                .name("A")
                .description("Description")
                .date(date)
                .users(Arrays.asList())
                .teacher(teacher)
                .createdAt(localDateTime)
                .updatedAt(localDateTime)
                .build();
        Session s2 = Session.builder()
                .id(1L)
                .name("A")
                .description("Description")
                .date(date)
                .users(Arrays.asList())
                .teacher(teacher)
                .createdAt(localDateTime)
                .updatedAt(localDateTime)
                .build();
        Session s3 = Session.builder()
                .id(2L)
                .name("B")
                .description("Description")
                .date(date)
                .users(Arrays.asList())
                .teacher(teacher)
                .createdAt(localDateTime)
                .updatedAt(localDateTime)
                .build();

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1, s3);
        assertEquals(s1, s1);
        assertNotEquals(s1, null);
        assertNotEquals(s1, "string");
    }

    @Test
    void gettersAndSetters_shouldWork() {
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();

        Session session = new Session();
        session.setId(1L);
        session.setName("Test");
        session.setDescription("Desc");
        session.setDate(date);
        session.setCreatedAt(now);
        session.setUpdatedAt(now);

        assertEquals(1L, session.getId());
        assertEquals("Test", session.getName());
        assertEquals("Desc", session.getDescription());
        assertEquals(date, session.getDate());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }

    @Test
    void equalsAndHashCode_sameId_shouldBeEqual() {
        Session s1 = new Session();
        s1.setId(1L);

        Session s2 = new Session();
        s2.setId(1L);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void equalsAndHashCode_differentId_shouldNotBeEqual() {
        Session s1 = new Session();
        s1.setId(1L);

        Session s2 = new Session();
        s2.setId(2L);

        assertNotEquals(s1, s2);
    }

    @Test
    void equals_sameObject_shouldBeTrue() {
        Session session = new Session();
        session.setId(1L);

        assertEquals(session, session);
    }

    @Test
    void equals_null_shouldBeFalse() {
        Session session = new Session();
        session.setId(1L);

        assertNotEquals(session, null);
    }

    @Test
    void equals_differentClass_shouldBeFalse() {
        Session session = new Session();
        session.setId(1L);

        assertNotEquals(session, "not a session");
    }

    @Test
    void equals_nullIds_shouldBeEqual() {
        Session s1 = new Session();
        Session s2 = new Session();

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void equals_oneNullId_shouldNotBeEqual() {
        Session s1 = new Session();
        s1.setId(1L);

        Session s2 = new Session();

        assertNotEquals(s1, s2);
    }

}
