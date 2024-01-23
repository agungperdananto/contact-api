package com.github.contact.contact.service;

import java.util.Objects;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.github.contact.contact.entity.User;
import com.github.contact.contact.model.RegisterUserRequest;
import com.github.contact.contact.repository.UserRepository;
import com.github.contact.contact.security.BCrypt;

import jakarta.validation.Validator;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    @Transactional
    public void register(RegisterUserRequest request){

        Set<ConstraintViolation<RegisterUserRequest>> constraintViolations =  validator.validate(request);
        if (constraintViolations.size() != 0){
            throw new ConstraintViolationException(constraintViolations);
        }

        if (userRepository.existsById(Objects.requireNonNull(request.getUsername()))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already registered");
        }

        User user =  new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);

    }

}
