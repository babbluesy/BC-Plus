package com.example.demo.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.Setter;

@Data
public class LoginRequest {
    @NotBlank
    @Email    
    private String email;

    @NotBlank
    private String password;
}