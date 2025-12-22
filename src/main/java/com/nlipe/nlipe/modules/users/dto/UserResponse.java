package com.nlipe.nlipe.modules.users.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String role;
    private String email;
    private String phone;
    private String userName;
}