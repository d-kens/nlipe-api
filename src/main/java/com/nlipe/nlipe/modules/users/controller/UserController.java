package com.nlipe.nlipe.modules.users.controller;

import com.nlipe.nlipe.common.dto.ErrorDto;
import com.nlipe.nlipe.common.dto.PaginationRequest;
import com.nlipe.nlipe.common.dto.PagingResult;
import com.nlipe.nlipe.common.exception.EmailAlreadyExistException;
import com.nlipe.nlipe.common.exception.PasswordMismatchException;
import com.nlipe.nlipe.modules.users.dto.ChangePasswordRequest;
import com.nlipe.nlipe.modules.users.dto.CreateUserDto;
import com.nlipe.nlipe.modules.users.dto.UpdateUserRequest;
import com.nlipe.nlipe.modules.users.dto.UserResponse;
import com.nlipe.nlipe.modules.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public PagingResult<UserResponse> getAllUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        PaginationRequest request = new PaginationRequest(page, size, sortField, direction);
        return userService.getAllUsers(request);
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            UriComponentsBuilder uriComponentsBuilder,
            @Valid @RequestBody CreateUserDto createUserDto
    ) {
        var response =  userService.createUser(createUserDto);
        var uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/{userId}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(userId, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(
            @PathVariable Long userId,
            @RequestBody UpdateUserRequest request
    ) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorDto> handleEmailAlreadyExistException(EmailAlreadyExistException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorDto(exception.getMessage())
        );
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorDto> handlePasswordMismatchException(PasswordMismatchException exception) {
        return ResponseEntity.badRequest().body(
                new ErrorDto(exception.getMessage())
        );
    }
}