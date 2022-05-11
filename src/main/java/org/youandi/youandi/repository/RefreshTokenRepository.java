package org.youandi.youandi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.youandi.youandi.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Long id);
}
