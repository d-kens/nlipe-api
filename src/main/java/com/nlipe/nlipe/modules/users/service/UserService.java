package com.nlipe.nlipe.modules.users.service;

import com.nlipe.nlipe.common.exception.EmailAlreadyExistException;
import com.nlipe.nlipe.common.exception.NotFoundException;
import com.nlipe.nlipe.modules.users.dto.CreateUserDto;
import com.nlipe.nlipe.modules.users.dto.UserResponse;
import com.nlipe.nlipe.modules.users.entity.User;
import com.nlipe.nlipe.modules.users.mapper.UserMapper;
import com.nlipe.nlipe.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    public UserResponse getUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("user with id " + userId + " not found")
        );

        return userMapper.toResponse(user);
    }

    public UserResponse createUser(CreateUserDto createUserDto) {

        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new EmailAlreadyExistException();
        }

        User user = userMapper.toEntity(createUserDto);
        user.setPasswordHash(passwordEncoder.encode(createUserDto.getPassword()));

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