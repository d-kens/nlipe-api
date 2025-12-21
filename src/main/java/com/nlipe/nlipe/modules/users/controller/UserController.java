package com.nlipe.nlipe.modules.users.controller;

import com.nlipe.nlipe.modules.users.dto.CreateUserDto;
import com.nlipe.nlipe.modules.users.entity.User;
import com.nlipe.nlipe.modules.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody CreateUserDto createUserDto) {
        System.out.println("-------------------------------------------------------------------------");
        System.out.println(createUserDto.toString());
        System.out.println("-------------------------------------------------------------------------");
        return userService.createUser(createUserDto);
    }
}