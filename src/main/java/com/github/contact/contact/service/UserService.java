package com.github.contact.contact.service;

import java.util.Objects;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.github.contact.contact.entity.User;
import com.github.contact.contact.model.RegisterUserRequest;
import com.github.contact.contact.model.UpdateUserRequest;
import com.github.contact.contact.model.UserResponse;
import com.github.contact.contact.repository.UserRepository;
import com.github.contact.contact.security.BCrypt;

import jakarta.transaction.Transactional;



@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterUserRequest request){

        validationService.validate(request);

        if (userRepository.existsById(Objects.requireNonNull(request.getUsername()))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already registered");
        }

        User user =  new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);

    }

    public UserResponse get(User user){
        return UserResponse.builder()
            .username(user.getUsername())
            .name(user.getName())
            .build();
    }   

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request){

        validationService.validate(request);
        
        if(Objects.nonNull(request.getName())){
            user.setName(request.getName());
        }

        if(Objects.nonNull(request.getPassword())){
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        Objects.requireNonNull(user);
        userRepository.save(user);

        return UserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .build();
    }

}
