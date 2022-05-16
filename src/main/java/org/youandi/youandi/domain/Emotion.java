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
    private EmotionType emotionType;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
