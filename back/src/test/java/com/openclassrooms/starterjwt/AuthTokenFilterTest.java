package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // --------------------------------------------------
    // ✅ CAS 1 : JWT valide → authentication créée
    // --------------------------------------------------
    @Test
    void doFilterInternal_shouldAuthenticateUser_whenJwtIsValid() throws Exception {
        String token = "valid.jwt.token";
        String username = "user@test.com";

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username(username)
                .password("password")
                .build();

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        when(jwtUtils.validateJwtToken(token))
                .thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(token))
                .thenReturn(username);
        when(userDetailsService.loadUserByUsername(username))
                .thenReturn(userDetails);

        authTokenFilter.doFilter(request, response, filterChain);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals(username, authentication.getName());

        verify(filterChain).doFilter(request, response);
    }

    // --------------------------------------------------
    // ✅ CAS 2 : Header Authorization absent
    // --------------------------------------------------
    @Test
    void doFilterInternal_shouldNotAuthenticate_whenAuthorizationHeaderIsMissing() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        authTokenFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtils, userDetailsService);
    }

    // --------------------------------------------------
    // ✅ CAS 3 : Header mal formé (pas Bearer)
    // --------------------------------------------------
    @Test
    void doFilterInternal_shouldNotAuthenticate_whenHeaderIsInvalid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic abc123");

        authTokenFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtils, userDetailsService);
    }

    // --------------------------------------------------
    // ✅ CAS 4 : JWT invalide
    // --------------------------------------------------
    @Test
    void doFilterInternal_shouldNotAuthenticate_whenJwtIsInvalid() throws Exception {
        String token = "invalid.jwt";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        when(jwtUtils.validateJwtToken(token))
                .thenReturn(false);

        authTokenFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    // --------------------------------------------------
    // ✅ CAS 5 : Exception → catch couvert
    // --------------------------------------------------
    @Test
    void doFilterInternal_shouldHandleException_andContinueFilterChain() throws Exception {
        String token = "jwt";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);
        when(jwtUtils.validateJwtToken(token))
                .thenThrow(new RuntimeException("Boom"));

        authTokenFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
