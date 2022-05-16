package org.youandi.youandi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.youandi.youandi.domain.Emotion;
import org.youandi.youandi.domain.Post;

import java.util.Optional;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
    Optional<Emotion> findByPost(Post post);
}
