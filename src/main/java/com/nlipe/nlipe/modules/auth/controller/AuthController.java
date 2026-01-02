package com.nlipe.nlipe.modules.auth.controller;

import com.nlipe.nlipe.modules.users.dto.UserResponse;
import com.nlipe.nlipe.security.config.JwtConfig;
import com.nlipe.nlipe.modules.auth.dto.AuthRequest;
import com.nlipe.nlipe.modules.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtConfig jwtConfig;
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<String> login(
            HttpServletResponse response,
            @Valid @RequestBody AuthRequest authRequest
    ) {
        var authResponse = authService.login(authRequest);

        var cookie = new Cookie("refreshToken", authResponse.getRefreshToken());
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh-token");
        cookie.setMaxAge(jwtConfig.getAccessTokenExpiration());

        response.addCookie(cookie);

        return ResponseEntity.ok(authResponse.getRefreshToken());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(
            @CookieValue(value = "refreshToken") String refreshToken
    ) {
        var accessToken = authService.refreshToken(refreshToken);

        if (accessToken == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(accessToken);
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();
        return authService.getCurrentUser(userId);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}