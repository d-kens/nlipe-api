package com.nlipe.nlipe.modules.auth.service;

import com.nlipe.nlipe.modules.auth.dto.AuthRequest;
import com.nlipe.nlipe.modules.auth.dto.AuthResponse;
import com.nlipe.nlipe.modules.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private JwtService jwtService;
    private UserService userService;
    private AuthenticationManager authenticationManager;

    public AuthResponse login(AuthRequest authRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        var user = userService.getUserByEmail(authRequest.getEmail());

        var accessTokenObject = jwtService.generateAccessToken(user);
        var refreshTokenObject = jwtService.generateRefreshToken(user);


        return new AuthResponse(
                accessTokenObject.toString(),
                refreshTokenObject.toString()
        );
    }

    public String refreshToken(String refreshToken) {
        var refreshTokenObject = jwtService.parseToken(refreshToken);

        if (refreshTokenObject == null || refreshTokenObject.isExpired())
            return null;


        var userEmail = refreshTokenObject.getUserEmail();
        var user = userService.getUserByEmail(userEmail);

        var accessTokenObject = jwtService.generateRefreshToken(user);
        return accessTokenObject.toString();

    }
}