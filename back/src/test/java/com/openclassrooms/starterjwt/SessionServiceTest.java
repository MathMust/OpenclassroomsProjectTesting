package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- TESTS PARTICIPATE ----------

    @Test
    void participate_shouldThrowNotFound_whenSessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                sessionService.participate(1L, 10L)
        );
    }

    @Test
    void participate_shouldThrowNotFound_whenUserNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(new Session()));
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                sessionService.participate(1L, 10L)
        );
    }

    @Test
    void participate_shouldThrowBadRequest_whenAlreadyParticipating() {
        User user = new User();
        user.setId(10L);

        Session session = new Session();
        session.setUsers(Arrays.asList(user));

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () ->
                sessionService.participate(1L, 10L)
        );
    }

    @Test
    void participate_shouldAddUser_whenValid() {
        User user = new User();
        user.setId(10L);

        Session session = new Session();
        session.setUsers(new ArrayList<>());

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        sessionService.participate(1L, 10L);

        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository).save(session);
    }

    // ---------- TESTS NO LONGER PARTICIPATE ----------

    @Test
    void noLongerParticipate_shouldThrowNotFound_whenSessionNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                sessionService.noLongerParticipate(1L, 10L)
        );
    }

    @Test
    void noLongerParticipate_shouldThrowBadRequest_whenUserNotParticipating() {
        Session session = new Session();
        session.setUsers(Arrays.asList());

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () ->
                sessionService.noLongerParticipate(1L, 10L)
        );
    }

    @Test
    void noLongerParticipate_shouldRemoveUser_whenValid() {
        User userToRemove = new User();
        userToRemove.setId(10L);

        User otherUser = new User();
        otherUser.setId(20L);

        Session session = new Session();
        session.setUsers(new ArrayList<>(Arrays.asList(userToRemove, otherUser)));

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(1L, 10L);

        // user 10 doit être retiré
        assertFalse(session.getUsers().contains(userToRemove));

        // user 20 doit rester → couvre la branche 'false'
        assertTrue(session.getUsers().contains(otherUser));

        verify(sessionRepository).save(session);
    }

    // ---------- TESTS MÉTHODES SIMPLES ----------

    @Test
    void create_shouldSaveSession() {
        Session session = new Session();

        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.create(session);

        assertEquals(session, result);
        verify(sessionRepository).save(session);
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        sessionService.delete(1L);

        verify(sessionRepository).deleteById(1L);
    }

    @Test
    void findAll_shouldReturnList() {
        List<Session> sessions = Arrays.asList(new Session(), new Session());

        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.findAll();

        assertEquals(sessions, result);
        verify(sessionRepository).findAll();
    }

    @Test
    void getById_shouldReturnSession_whenFound() {
        Session session = new Session();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session result = sessionService.getById(1L);

        assertEquals(session, result);
    }

    @Test
    void getById_shouldReturnNull_whenNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        Session result = sessionService.getById(1L);

        assertNull(result);
    }

    @Test
    void update_shouldSetIdAndSave() {
        Session session = new Session();
        Session savedSession = new Session();
        savedSession.setId(1L);

        when(sessionRepository.save(session)).thenReturn(savedSession);

        Session result = sessionService.update(1L, session);

        assertEquals(savedSession, result);
        assertEquals(1L, session.getId());
        verify(sessionRepository).save(session);
    }

}
