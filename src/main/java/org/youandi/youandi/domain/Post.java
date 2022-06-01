package org.youandi.youandi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.youandi.youandi.dto.PostRequestDto;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Post extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private String region;

    public Post(PostRequestDto postRequestDto, User user, String region) {
        this.content = postRequestDto.getContent();
        this.latitude = postRequestDto.getLatitude();
        this.longitude = postRequestDto.getLongitude();
        this.user = user;
        this.region = region;
    }
}
