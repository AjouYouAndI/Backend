package org.youandi.youandi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.youandi.youandi.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

}
