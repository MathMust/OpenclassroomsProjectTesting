package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    void shouldBuildTeacherWithBuilder() {
        LocalDateTime localDateTime = LocalDateTime.now();

        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(localDateTime)
                .updatedAt(localDateTime)
                .build();

        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Doe", teacher.getLastName());
        assertEquals(localDateTime, teacher.getCreatedAt());
        assertEquals(localDateTime, teacher.getUpdatedAt());
    }

    @Test
    void shouldSupportChainedAccessors() {
        Teacher teacher = new Teacher()
                .setId(1L)
                .setFirstName("Jane")
                .setLastName("Smith");

        assertEquals(1L, teacher.getId());
        assertEquals("Jane", teacher.getFirstName());
        assertEquals("Smith", teacher.getLastName());
    }

    @Test
    void equals_shouldBeBasedOnIdOnly() {
        Teacher t1 = Teacher.builder().id(1L).firstName("A").lastName("B").build();
        Teacher t2 = Teacher.builder().id(1L).firstName("X").lastName("Y").build();
        Teacher t3 = Teacher.builder().id(2L).firstName("A").lastName("B").build();

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
        assertNotEquals(t1, t3);
        assertEquals(t1, t1);
        assertNotEquals(t1, null);
        assertNotEquals(t1, "string");
    }

    @Test
    void gettersAndSetters_shouldWork() {
        LocalDateTime now = LocalDateTime.now();

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Doe", teacher.getLastName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @Test
    void equalsAndHashCode_sameId_shouldBeEqual() {
        Teacher t1 = new Teacher();
        t1.setId(1L);

        Teacher t2 = new Teacher();
        t2.setId(1L);

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void equalsAndHashCode_differentId_shouldNotBeEqual() {
        Teacher t1 = new Teacher();
        t1.setId(1L);

        Teacher t2 = new Teacher();
        t2.setId(2L);

        assertNotEquals(t1, t2);
    }

    @Test
    void equals_sameObject_shouldBeTrue() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        assertEquals(teacher, teacher);
    }

    @Test
    void equals_null_shouldBeFalse() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        assertNotEquals(teacher, null);
    }

    @Test
    void equals_differentClass_shouldBeFalse() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        assertNotEquals(teacher, "not a teacher");
    }

    @Test
    void equals_nullIds_shouldBeEqual() {
        Teacher t1 = new Teacher();
        Teacher t2 = new Teacher();

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void equals_oneNullId_shouldNotBeEqual() {
        Teacher t1 = new Teacher();
        t1.setId(1L);

        Teacher t2 = new Teacher();

        assertNotEquals(t1, t2);
    }

}
