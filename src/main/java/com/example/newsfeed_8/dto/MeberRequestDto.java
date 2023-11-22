package com.example.newsfeed_8.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeberRequestDto {
    @Pattern(regexp = "^[a-z0-9]{4,10}")
    private String userId;

    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}")
    private String password;

    @Email
    private String email;

    @Size(min=1, max=30)
    private String introduction;
}
