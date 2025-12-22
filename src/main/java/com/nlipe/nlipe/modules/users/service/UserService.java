package com.nlipe.nlipe.modules.users.service;


import com.nlipe.nlipe.common.exception.EmailAlreadyExistException;
import com.nlipe.nlipe.modules.users.dto.CreateUserDto;
import com.nlipe.nlipe.modules.users.dto.UserResponse;
import com.nlipe.nlipe.modules.users.entity.User;
import com.nlipe.nlipe.modules.users.mapper.UserMapper;
import com.nlipe.nlipe.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponse createUser(CreateUserDto createUserDto) {

        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new EmailAlreadyExistException();
        }

        User user = userMapper.toEntity(createUserDto);
        user.setPasswordHash(passwordEncoder.encode(createUserDto.getPassword()));

        userRepository.save(user);

        return userMapper.toResponse(user);
    }
}