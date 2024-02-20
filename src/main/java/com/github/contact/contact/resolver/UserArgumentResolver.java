package com.github.contact.contact.resolver;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import com.github.contact.contact.entity.User;
import com.github.contact.contact.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver{

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supportsParameter(@Nullable MethodParameter parameter) {
        Objects.requireNonNull(parameter);
        return User.class.equals(parameter.getParameterType());
    }

    @Override
    @Nullable
    public Object resolveArgument(@Nullable MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
            @Nullable NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
                Objects.requireNonNull(webRequest);
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();

        String token = servletRequest.getHeader("X-API-TOKEN");

        if (token == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unautorized");
        }

        User user =  userRepository.findFirstByToken(token).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unautorized"));

        if (user.getTokenExpiredAt() < System.currentTimeMillis()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unautorized");
        }

        return user;
    }

    



}
