package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    private final String jwtSecret = "test-secret-key";
    private final int jwtExpirationMs = 1000; // 1 seconde

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();

        // Injection des @Value
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }

    @Test
    void generateJwtToken_shouldGenerateValidToken() {
        // Arrange
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .admin(false)
                .build();

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Act
        String token = jwtUtils.generateJwtToken(authentication);

        // Assert
        assertNotNull(token);
        assertEquals("test@example.com", jwtUtils.getUserNameFromJwtToken(token));
    }

    @Test
    void getUserNameFromJwtToken_shouldReturnUsername() {
        // Arrange
        String token = Jwts.builder()
                .setSubject("user@test.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Act
        String username = jwtUtils.getUserNameFromJwtToken(token);

        // Assert
        assertEquals("user@test.com", username);
    }

    @Test
    void validateJwtToken_shouldReturnTrue_whenTokenIsValid() {
        String token = Jwts.builder()
                .setSubject("user@test.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void validateJwtToken_shouldReturnFalse_whenTokenIsExpired() {
        String token = Jwts.builder()
                .setSubject("user@test.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 2000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    void validateJwtToken_shouldReturnFalse_whenTokenIsInvalid() {
        String invalidToken = "invalid.token.value";

        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }

    @Test
    void validateJwtToken_shouldReturnFalse_whenSignatureIsInvalid() {
        // Token signé avec une AUTRE clé
        String tokenWithWrongSecret = Jwts.builder()
                .setSubject("user@test.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(SignatureAlgorithm.HS512, "wrong-secret")
                .compact();

        boolean result = jwtUtils.validateJwtToken(tokenWithWrongSecret);

        assertFalse(result);
    }

    @Test
    void validateJwtToken_shouldReturnFalse_whenTokenIsUnsupported() {
        // JWT chiffré (JWE) -> non supporté par parser JWS
        String unsupportedToken =
                "eyJhbGciOiJub25lIn0." +
                        "eyJzdWIiOiJ1c2VyQHRlc3QuY29tIn0.";

        boolean result = jwtUtils.validateJwtToken(unsupportedToken);

        assertFalse(result);
    }

    @Test
    void validateJwtToken_shouldReturnFalse_whenTokenIsEmpty() {
        assertFalse(jwtUtils.validateJwtToken(""));
    }


}
