package com.nlipe.nlipe.modules.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "old password is required")
    private String oldPassword;

    @NotBlank(message = "new password is required")
    @Size(min = 8, max = 20, message = "password must be between 8 and 20 characters")
    private String newPassword;
}