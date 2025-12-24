package com.nlipe.nlipe.modules.auth.controller;

import com.nlipe.nlipe.common.dto.ErrorDto;
import com.nlipe.nlipe.modules.auth.dto.AuthRequest;
import com.nlipe.nlipe.modules.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    @PostMapping("login")
    public String login(@Valid @RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}