package org.youandi.youandi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class PostRequestDto {
    private String content;
    private double latitude;
    private double longitude;
}
