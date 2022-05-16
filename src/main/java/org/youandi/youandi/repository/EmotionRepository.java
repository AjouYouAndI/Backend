package org.youandi.youandi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.youandi.youandi.domain.Emotion;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
}
