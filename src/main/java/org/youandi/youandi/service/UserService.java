package org.youandi.youandi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.youandi.youandi.advice.CAccountExistedException;
import org.youandi.youandi.advice.CRefreshTokenException;
import org.youandi.youandi.advice.CSigninFailedException;
import org.youandi.youandi.advice.CUsernameNotFoundException;
import org.youandi.youandi.domain.Role;
import org.youandi.youandi.domain.User;
import org.youandi.youandi.domain.RefreshToken;
import org.youandi.youandi.dto.SignUpRequestDto;
import org.youandi.youandi.dto.TokenDto;
import org.youandi.youandi.dto.TokenRequestDto;
import org.youandi.youandi.dto.UserResponseDto;
import org.youandi.youandi.repository.RefreshTokenRepository;
import org.youandi.youandi.repository.UserRepository;
import org.youandi.youandi.security.JwtTokenProvider;

import javax.transaction.Transactional;
import java.security.cert.CertificateRevokedException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userReposiroty;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenDto signin(String email, String password) {
        User user = userReposiroty.findByEmail(email).orElseThrow(CSigninFailedException::new);
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new CSigninFailedException();
        }
        TokenDto tokenDto = jwtTokenProvider.createToken(user.getEmail(), user.getRole());

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }

    @Transactional
    public User signup(SignUpRequestDto requestDto, boolean isAdmin) {
        Optional<User> foundUser = userReposiroty.findByEmail(requestDto.getEmail());
        if (foundUser.isPresent()) {
            throw new CAccountExistedException();
        }
        String endcodedPw = passwordEncoder.encode(requestDto.getPassword());
        Role role = Role.USER;
        if (isAdmin) {
            role = Role.AMIN;
        }
        User user = new User(requestDto.getName(), requestDto.getEmail() ,endcodedPw, role);
        return userReposiroty.save(user);
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto requestDto) {
        if(!jwtTokenProvider.validateToken(requestDto.getRefreshToken())) {
            throw new CRefreshTokenException();
        }

        String accessToken = requestDto.getAccessToken();
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        User user = userReposiroty.findByEmail(authentication.getName()).orElseThrow(CUsernameNotFoundException::new);
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId()).orElseThrow(CRefreshTokenException::new);

        if (!refreshToken.getToken().equals(refreshToken.getToken()))
            throw new CRefreshTokenException();

        TokenDto newCreatedToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole());
        RefreshToken updatedRefreshToken = refreshToken.updateToken(newCreatedToken.getRefreshToken());
        refreshTokenRepository.save(updatedRefreshToken);

        return newCreatedToken;
    }

    public UserResponseDto findByEmail(String email) {
        User user = userReposiroty.findByEmail(email).orElseThrow(CUsernameNotFoundException::new);
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }



}
