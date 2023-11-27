package com.example.newsfeed_8.Member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateEmailRequestDto {

        @Email
        private String email;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePasswordRequestDto {

        @Pattern(regexp = "^[a-zA-Z0-9]{8,15}")
        private String originPassword;

        @Pattern(regexp = "^[a-zA-Z0-9]{8,15}")
        private String newPassword;

        @Pattern(regexp = "^[a-zA-Z0-9]{8,15}")
        private String newPasswordCheck;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateIntroductionRequestDto {

        @Size(min = 1, max = 30)
        private String introduction;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMyAccountResponseDto {

        private String userId;
        private String email;
        private String introduction;
    }

}
