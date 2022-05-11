package org.youandi.youandi.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignUpRequestDto {
    private String name;
    private String email;
    private String password;
}
