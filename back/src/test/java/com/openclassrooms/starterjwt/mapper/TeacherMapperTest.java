package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMapperTest {

    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    @Test
    void testToDto() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        TeacherDto dto = teacherMapper.toDto(teacher);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
    }

    @Test
    void testToDto_TeacherNull() {
        TeacherDto dto = teacherMapper.toDto((Teacher) null);
        assertNull(dto);
    }

    @Test
    void testToEntity() {
        TeacherDto dto = new TeacherDto();
        dto.setId(2L);
        dto.setFirstName("Jane");
        dto.setLastName("Smith");

        Teacher teacher = teacherMapper.toEntity(dto);

        assertNotNull(teacher);
        assertEquals(2L, teacher.getId());
        assertEquals("Jane", teacher.getFirstName());
        assertEquals("Smith", teacher.getLastName());
    }

    @Test
    void testToEntity_TeacherDtoNull() {
        Teacher teacher = teacherMapper.toEntity((TeacherDto) null);
        assertNull(teacher);
    }

    @Test
    void testToDtoList() {
        Teacher t1 = Teacher.builder().id(1L).firstName("John").lastName("Doe").build();
        Teacher t2 = Teacher.builder().id(2L).firstName("Jane").lastName("Smith").build();

        List<Teacher> teachers = Arrays.asList(t1, t2);
        List<TeacherDto> dtos = teacherMapper.toDto(teachers);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("John", dtos.get(0).getFirstName());
        assertEquals("Smith", dtos.get(1).getLastName());
    }

    @Test
    void testToDtoList_ListTeacherNull() {
        List<TeacherDto> dtos = teacherMapper.toDto((List<Teacher>) null);
        assertNull(dtos);
    }

    @Test
    void testToEntityList() {
        TeacherDto dto1 = new TeacherDto();
        dto1.setId(1L);
        dto1.setFirstName("John");
        dto1.setLastName("Doe");

        TeacherDto dto2 = new TeacherDto();
        dto2.setId(2L);
        dto2.setFirstName("Jane");
        dto2.setLastName("Smith");

        List<TeacherDto> dtos = Arrays.asList(dto1, dto2);
        List<Teacher> teachers = teacherMapper.toEntity(dtos);

        assertNotNull(teachers);
        assertEquals(2, teachers.size());
        assertEquals("John", teachers.get(0).getFirstName());
        assertEquals("Smith", teachers.get(1).getLastName());
    }

    @Test
    void testToEntityList_ListTeacherDtoNull() {
        List<Teacher> teachers = teacherMapper.toEntity((List<TeacherDto>) null);
        assertNull(teachers);
    }
}
