package com.nlipe.nlipe.modules.users.service;

import com.nlipe.nlipe.common.dto.PaginationRequest;
import com.nlipe.nlipe.common.dto.PagingResult;
import com.nlipe.nlipe.common.exception.EmailAlreadyExistException;
import com.nlipe.nlipe.common.exception.NotFoundException;
import com.nlipe.nlipe.common.exception.PasswordMismatchException;
import com.nlipe.nlipe.common.utils.PaginationUtils;
import com.nlipe.nlipe.modules.users.dto.ChangePasswordRequest;
import com.nlipe.nlipe.modules.users.dto.CreateUserDto;
import com.nlipe.nlipe.modules.users.dto.UpdateUserRequest;
import com.nlipe.nlipe.modules.users.dto.UserResponse;
import com.nlipe.nlipe.modules.users.entity.User;
import com.nlipe.nlipe.modules.users.enums.Role;
import com.nlipe.nlipe.modules.users.mapper.UserMapper;
import com.nlipe.nlipe.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(
                () -> new NotFoundException("user with email " + email + "not found")
        );
    }

    public PagingResult<UserResponse> getAllUsers(PaginationRequest request) {
        final Pageable pageable = PaginationUtils.getPageable(request);
        final Page<User> users = userRepository.findAll(pageable);
        final List<UserResponse> userResponses = users.stream().map(userMapper::toResponse).toList();

        return new PagingResult<>(
                userResponses,
                users.getTotalPages(),
                users.getTotalElements(),
                users.getSize(),
                users.getNumber(),
                users.isEmpty()
        );
    }

    public UserResponse getUserById(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("user with id " + userId + " not found")
        );

        return userMapper.toResponse(user);
    }

    public UserResponse createUser(CreateUserDto dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistException();
        }

        User user = userMapper.toEntity(dto);

        if (dto.getRole() == null)
            user.setRole(Role.USER);
        else
            user.setRole(Role.valueOf(dto.getRole().trim().toUpperCase()));

        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        var user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("user with id " + userId + " not found")
        );

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash()))
            throw new PasswordMismatchException();

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        var user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("user with id " + userId + " not found")
        );

        if (userRepository.existsByEmailAndIdNot(request.getEmail(), userId))
            throw new EmailAlreadyExistException();

        userMapper.update(request, user);

        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("user with id " + userId + " not found")
        );

        userRepository.delete(user);
    }
}