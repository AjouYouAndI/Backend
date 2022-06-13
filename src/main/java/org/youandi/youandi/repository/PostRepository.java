package org.youandi.youandi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.youandi.youandi.domain.Post;
import org.youandi.youandi.domain.User;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUser(User user);
    List<Post> findAllByRegion(String region);
}
