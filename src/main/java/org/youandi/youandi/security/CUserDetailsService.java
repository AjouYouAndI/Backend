package org.youandi.youandi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.youandi.youandi.advice.CUsernameNotFoundException;
import org.youandi.youandi.domain.User;
import org.youandi.youandi.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class CUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) {
        return userRepository.findByEmail(userEmail).orElseThrow(CUsernameNotFoundException::new);
    }
}
