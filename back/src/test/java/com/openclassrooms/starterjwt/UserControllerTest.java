package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    // ------------------ GET /api/user/{id} ------------------
    @Test
    @WithMockUser(username = "test@example.com")
    void findById_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void findById_shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void findById_shouldReturnOk_whenUserExists() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setLastName("lastname");
        user.setFirstName("firstname");
        user = userRepository.save(user);

        mockMvc.perform(get("/api/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // ------------------ DELETE /api/user/{id} ------------------
    @Test
    @WithMockUser(username = "test@example.com")
    void delete_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "other@example.com")
    void delete_shouldReturnUnauthorized_whenUserIsNotOwner() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user = userRepository.save(user);

        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void delete_shouldReturnOk_whenUserIsOwner() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user = userRepository.save(user);

        mockMvc.perform(delete("/api/user/" + user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@example.com")
    void delete_shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
        mockMvc.perform(delete("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }
}
