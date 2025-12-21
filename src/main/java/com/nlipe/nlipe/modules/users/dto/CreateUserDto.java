package com.nlipe.nlipe.modules.users.dto;

import lombok.Data;

@Data
public class CreateUserDto {
    private String role;
    private String email;
    private String phone;
    private String password;
    private String userName;
}