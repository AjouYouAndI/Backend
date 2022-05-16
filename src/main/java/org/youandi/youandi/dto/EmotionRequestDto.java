package org.youandi.youandi.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.youandi.youandi.domain.EmotionType;

@Getter
@RequiredArgsConstructor
public class EmotionRequestDto {
    private Long postId;
    private EmotionType emotionType;
}
