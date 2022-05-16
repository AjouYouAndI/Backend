package org.youandi.youandi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Emotion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EmotionType emotionType;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Emotion(EmotionType emotionType, Post post) {
        this.emotionType = emotionType;
        this.post = post;
    }
}
