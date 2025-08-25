package com.spring.guide.linkdrop.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    @NotBlank
    @Size(min = 6, max = 20)
    private String username;

    @Size(min = 6)
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/`~|\\\\]+$", message = "Password can contain only digits, alphabets, and allowed special characters")
    @NotBlank
    private String password;
}
