package org.youandi.youandi.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignInRequestDto {
    private String email;
    private String password;
}
