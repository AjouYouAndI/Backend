package org.youandi.youandi.dto;

import lombok.Getter;
import org.youandi.youandi.domain.EmotionType;
import org.youandi.youandi.domain.Post;

@Getter
public class PostListResponseDto {
    private String title;
    private String content;
    private EmotionType emotionType;

    public PostListResponseDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.emotionType = post.getEmotionType();
    }
}
