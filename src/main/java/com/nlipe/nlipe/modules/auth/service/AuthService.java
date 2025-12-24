package com.nlipe.nlipe.modules.auth.service;

import com.nlipe.nlipe.modules.auth.dto.AuthRequest;
import com.nlipe.nlipe.modules.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private UserService userService;
    private AuthenticationManager authenticationManager;

    public String login(AuthRequest authRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        // TODO: Generate JWT tokens

        return "This is your access token";
    }
}