package com.nlipe.nlipe.modules.users.mapper;

import com.nlipe.nlipe.modules.users.dto.CreateUserDto;
import com.nlipe.nlipe.modules.users.dto.UserResponse;
import com.nlipe.nlipe.modules.users.entity.User;
import com.nlipe.nlipe.modules.users.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserDto dto) {
        User user = new User();
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setUserName(dto.getUserName());

        String roleInput = dto.getRole();
        if (roleInput == null || roleInput.isBlank()) {
            user.setRole(Role.USER);
        } else {
            user.setRole(Role.valueOf(roleInput.trim().toUpperCase()));
        }
        return user;
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getRole() != null ? user.getRole().toString() : null,
                user.getEmail(),
                user.getPhone(),
                user.getUserName()
        );
    }
}
