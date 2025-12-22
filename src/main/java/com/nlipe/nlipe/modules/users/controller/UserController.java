package com.nlipe.nlipe.modules.users.controller;

import com.nlipe.nlipe.common.dto.ErrorDto;
import com.nlipe.nlipe.common.exception.EmailAlreadyExistException;
import com.nlipe.nlipe.modules.users.dto.CreateUserDto;
import com.nlipe.nlipe.modules.users.entity.User;
import com.nlipe.nlipe.modules.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        return userService.createUser(createUserDto);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorDto> handleEmailAlreadyExistException(EmailAlreadyExistException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorDto(exception.getMessage())
        );
    }
}