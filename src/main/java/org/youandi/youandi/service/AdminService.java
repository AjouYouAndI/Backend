package org.youandi.youandi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.youandi.youandi.advice.CAccessDeniedException;
import org.youandi.youandi.advice.CUsernameNotFoundException;
import org.youandi.youandi.domain.Role;
import org.youandi.youandi.domain.User;
import org.youandi.youandi.repository.PostRepository;
import org.youandi.youandi.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public List<User> findAllUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(CUsernameNotFoundException::new);
        if(user.getRole().getKey() != Role.AMIN.getKey()) {
            throw new CAccessDeniedException();
        }
        return userRepository.findAll();
    }

    public void deleteAllPosts(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(CUsernameNotFoundException::new);
        if(user.getRole().getKey() != Role.AMIN.getKey()) {
            throw new CAccessDeniedException();
        }
        postRepository.deleteAll();
    }

}
