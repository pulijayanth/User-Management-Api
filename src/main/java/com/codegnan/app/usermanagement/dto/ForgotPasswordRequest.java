package com.codegnan.app.usermanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForgotPasswordRequest(
        @NotBlank @Email @Size(max = 190) String username,
        @NotBlank @Size(min = 8, max = 100) String newPassword
) {}
