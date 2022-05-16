package org.youandi.youandi.service;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.youandi.youandi.advice.CAuthenticationEntryPointException;
import org.youandi.youandi.advice.CPostNotExistedException;
import org.youandi.youandi.advice.CUsernameNotFoundException;
import org.youandi.youandi.domain.Post;
import org.youandi.youandi.domain.User;
import org.youandi.youandi.dto.PostRequestDto;
import org.youandi.youandi.repository.PostRepository;
import org.youandi.youandi.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    public Post createPost(@NotNull PostRequestDto postRequestDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(CUsernameNotFoundException::new);
        Post post = new Post(postRequestDto, user);
        postRepository.save(post);
        return post;
    }

    public Post getPostByIdx (Long id) {
        Post post = postRepository.findById(id).orElseThrow(CPostNotExistedException::new);
        return post;
    }

    public List<Post> getPostByUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(CUsernameNotFoundException::new);
        return postRepository.findAllByUser(user);
    }

    public Long deletePost(Long id, String email) {
        Post post = postRepository.findById(id).orElseThrow(CPostNotExistedException::new);
        User user = userRepository.findByEmail(email).orElseThrow(CUsernameNotFoundException::new);
        if (!post.getUser().getId().equals(user.getId())) {
            throw new CAuthenticationEntryPointException();
        }
        postRepository.delete(post);
        return id;
    }
}
