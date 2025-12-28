package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionMapperTest {

    private SessionMapper sessionMapper;
    private TeacherService teacherService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        teacherService = mock(TeacherService.class);
        userService = mock(UserService.class);

        // créer le mapper et injecter les services mockés
        sessionMapper = Mappers.getMapper(SessionMapper.class);
        ((SessionMapper) sessionMapper).teacherService = teacherService;
        ((SessionMapper) sessionMapper).userService = userService;
    }

    @Test
    void testToEntity() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherService.findById(1L)).thenReturn(teacher);

        User user1 = new User(); user1.setId(10L);
        User user2 = new User(); user2.setId(20L);
        when(userService.findById(10L)).thenReturn(user1);
        when(userService.findById(20L)).thenReturn(user2);

        SessionDto dto = new SessionDto();
        dto.setTeacher_id(1L);
        dto.setUsers(Arrays.asList(10L, 20L));
        dto.setDescription("Description test");

        Session session = sessionMapper.toEntity(dto);

        assertNotNull(session);
        assertEquals("Description test", session.getDescription());
        assertEquals(1L, session.getTeacher().getId());
        assertEquals(2, session.getUsers().size());
        assertEquals(10L, session.getUsers().get(0).getId());
        assertEquals(20L, session.getUsers().get(1).getId());
    }

    @Test
    void testToEntity_SessionDtoNull() {
        Session session = sessionMapper.toEntity((SessionDto) null);
        assertNull(session);
    }

    @Test
    void testToEntityWithNulls() {
        SessionDto dto = new SessionDto();
        dto.setUsers(null);
        dto.setTeacher_id(null);

        Session session = sessionMapper.toEntity(dto);

        assertNotNull(session);
        assertNull(session.getTeacher());
        assertNotNull(session.getUsers());
        assertEquals(0, session.getUsers().size());
    }

    @Test
    void testToDto() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user1 = new User(); user1.setId(10L);
        User user2 = new User(); user2.setId(20L);

        Session session = new Session();
        session.setTeacher(teacher);
        session.setUsers(Arrays.asList(user1, user2));
        session.setDescription("Description test");

        SessionDto dto = sessionMapper.toDto(session);

        assertNotNull(dto);
        assertEquals("Description test", dto.getDescription());
        assertEquals(1L, dto.getTeacher_id());
        assertEquals(2, dto.getUsers().size());
        assertEquals(10L, dto.getUsers().get(0));
        assertEquals(20L, dto.getUsers().get(1));
    }

    @Test
    void testToDto_SessionNull() {
        SessionDto dto = sessionMapper.toDto((Session) null);
        assertNull(dto);
    }

    @Test
    void testToDtoWithNulls() {
        Session session = new Session();
        session.setTeacher(null);
        session.setUsers(null);
        session.setDescription("desc");

        SessionDto dto = sessionMapper.toDto(session);

        assertNotNull(dto);
        assertEquals("desc", dto.getDescription());
        assertNull(dto.getTeacher_id());
        assertNotNull(dto.getUsers());
        assertEquals(0, dto.getUsers().size());
    }

    @Test
    void testToEntityList() {
        Teacher teacher = new Teacher(); teacher.setId(1L);
        when(teacherService.findById(1L)).thenReturn(teacher);

        User user1 = new User(); user1.setId(10L);
        User user2 = new User(); user2.setId(20L);
        when(userService.findById(10L)).thenReturn(user1);
        when(userService.findById(20L)).thenReturn(user2);

        SessionDto dto1 = new SessionDto();
        dto1.setTeacher_id(1L);
        dto1.setUsers(Arrays.asList(10L, 20L));
        dto1.setDescription("Session 1");

        SessionDto dto2 = new SessionDto();
        dto2.setTeacher_id(1L);
        dto2.setUsers(Collections.singletonList(10L));
        dto2.setDescription("Session 2");

        List<SessionDto> dtoList = Arrays.asList(dto1, dto2);
        List<Session> sessions = sessionMapper.toEntity(dtoList);

        assertEquals(2, sessions.size());
        assertEquals("Session 1", sessions.get(0).getDescription());
        assertEquals("Session 2", sessions.get(1).getDescription());
        assertEquals(teacher, sessions.get(0).getTeacher());
        assertEquals(2, sessions.get(0).getUsers().size());
        assertEquals(1, sessions.get(1).getUsers().size());
    }

    @Test
    void testToEntityList_ListSessionDtoNull() {
        List<Session> sessions = sessionMapper.toEntity((List<SessionDto>) null);
        assertNull(sessions);
    }

    @Test
    void testToDtoList() {
        Teacher teacher = new Teacher(); teacher.setId(1L);

        User user1 = new User(); user1.setId(10L);
        User user2 = new User(); user2.setId(20L);

        Session session1 = new Session();
        session1.setTeacher(teacher);
        session1.setUsers(Arrays.asList(user1, user2));
        session1.setDescription("Session 1");

        Session session2 = new Session();
        session2.setTeacher(teacher);
        session2.setUsers(Collections.singletonList(user1));
        session2.setDescription("Session 2");

        List<Session> sessionList = Arrays.asList(session1, session2);
        List<SessionDto> dtoList = sessionMapper.toDto(sessionList);

        assertEquals(2, dtoList.size());
        assertEquals("Session 1", dtoList.get(0).getDescription());
        assertEquals("Session 2", dtoList.get(1).getDescription());
        assertEquals(2, dtoList.get(0).getUsers().size());
        assertEquals(1, dtoList.get(1).getUsers().size());
    }

    @Test
    void testToDtoList_ListSessionNull() {
        List<SessionDto> dtoList = sessionMapper.toDto((List<Session>) null);
        assertNull(dtoList);
    }

}
