package org.youandi.youandi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.youandi.youandi.advice.CAuthenticationEntryPointException;
import org.youandi.youandi.advice.CEmotionResultFailedException;
import org.youandi.youandi.advice.CPostNotExistedException;
import org.youandi.youandi.advice.CUsernameNotFoundException;
import org.youandi.youandi.domain.Emotion;
import org.youandi.youandi.domain.Post;
import org.youandi.youandi.domain.User;
import org.youandi.youandi.dto.EmotionRequestDto;
import org.youandi.youandi.repository.EmotionRepository;
import org.youandi.youandi.repository.PostRepository;
import org.youandi.youandi.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmotionService {

    private final EmotionRepository emotionRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Emotion addEmotionToPost(EmotionRequestDto requestDto){
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(CPostNotExistedException::new);
        Emotion emotion = new Emotion(requestDto.getEmotionType(), post);
        emotionRepository.save(emotion);
        return emotion;
    }

    public Emotion getEmotionByPost(String email, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(CPostNotExistedException::new);
        User user = userRepository.findByEmail(email).orElseThrow(CUsernameNotFoundException::new);
        if(!post.getUser().getId().equals(user.getId())) {
            throw new CAuthenticationEntryPointException();
        }
        Optional<Emotion> emotion = emotionRepository.findByPost(post);
        if(!emotion.isPresent()) {
            throw new CEmotionResultFailedException();
        }
        return emotion.get();
    }
}
