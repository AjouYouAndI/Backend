package org.youandi.youandi.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostRequestDto {
    private String content;
    private double latitude;
    private double longitude;
}
