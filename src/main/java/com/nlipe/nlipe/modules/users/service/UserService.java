package com.nlipe.nlipe.modules.users.service;


import com.nlipe.nlipe.modules.users.dto.CreateUserDto;
import com.nlipe.nlipe.modules.users.entity.User;
import com.nlipe.nlipe.modules.users.enums.Role;
import com.nlipe.nlipe.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(CreateUserDto createUserDto) {

        var user = new User();

        user.setPhone(createUserDto.getPhone());
        user.setEmail(createUserDto.getEmail());
        user.setUserName(createUserDto.getUserName());
        user.setRole(Role.valueOf(createUserDto.getRole()));
        user.setPasswordHash(passwordEncoder.encode(createUserDto.getPassword()));

        System.out.println("-------------------------------------------------------------------------");
        System.out.println(user.toString());
        System.out.println("-------------------------------------------------------------------------");

        return userRepository.save(user);
    }
}