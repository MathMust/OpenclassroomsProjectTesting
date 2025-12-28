package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToDto() {
        User user = new User("test@example.com", "Doe", "John", "password", false);

        UserDto dto = userMapper.toDto(user);

        assertNotNull(dto);
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.isAdmin(), dto.isAdmin());
    }

    @Test
    void testToDto_UserNull() {
        UserDto dto = userMapper.toDto((User) null);
        assertNull(dto);
    }

    @Test
    void testToEntity() {
        UserDto dto = new UserDto();
        dto.setEmail("test@example.com");
        dto.setPassword("password");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAdmin(false);

        User user = userMapper.toEntity(dto);

        assertNotNull(user);
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getFirstName(), user.getFirstName());
        assertEquals(dto.getLastName(), user.getLastName());
        assertEquals(dto.isAdmin(), user.isAdmin());
    }

    @Test
    void testToEntity_UserDtoNull() {
      User user = userMapper.toEntity((UserDto) null);
      assertNull(user);
    }

    @Test
    void testToDtoList() {
        User user1 = new User("user1@example.com", "Doe", "John", "pass1", false);
        User user2 = new User("user2@example.com", "Smith", "Jane", "pass2", true);

        List<User> users = Arrays.asList(user1, user2);

        List<UserDto> dtos = userMapper.toDto(users);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("user1@example.com", dtos.get(0).getEmail());
        assertEquals("user2@example.com", dtos.get(1).getEmail());
    }

    @Test
    void testToDtoList_ListUserNull() {
        List<UserDto> dtos = userMapper.toDto((List<User>) null);
        assertNull(dtos);
    }

    @Test
    void testToEntityList() {
        UserDto dto1 = new UserDto();
        dto1.setEmail("user1@example.com");
        dto1.setFirstName("John");
        dto1.setLastName("Doe");
        dto1.setPassword("pass1");
        dto1.setAdmin(false);

        UserDto dto2 = new UserDto();
        dto2.setEmail("user2@example.com");
        dto2.setFirstName("Jane");
        dto2.setLastName("Smith");
        dto2.setPassword("pass2");
        dto2.setAdmin(true);

        List<UserDto> dtos = Arrays.asList(dto1, dto2);

        List<User> users = userMapper.toEntity(dtos);

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("user1@example.com", users.get(0).getEmail());
        assertEquals("user2@example.com", users.get(1).getEmail());
        assertEquals("Jane", users.get(1).getFirstName());
    }

    @Test
    void testToEntityList_ListUserDtoNull() {
        List<User> users = userMapper.toEntity((List<UserDto>) null);
        assertNull(users);
    }
}
