package com.github.contact.contact.service;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.github.contact.contact.entity.User;
import com.github.contact.contact.model.LoginUserRequest;
import com.github.contact.contact.model.TokenResponse;
import com.github.contact.contact.repository.UserRepository;
import com.github.contact.contact.security.BCrypt;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginUserRequest request){
        
        validationService.validate(request);

        User user = userRepository.findById(Objects.requireNonNull(request.getUsername())).orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password is invalid."));

        if (BCrypt.checkpw(request.getPassword(), user.getPassword())){
            // success
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(System.currentTimeMillis() + (1000 * 16 * 24 * 30));
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        } else {
            // failed
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password is invalid");
        }

    }

    @Transactional
    public void logout(User user){
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepository.save(user);
    }
}
