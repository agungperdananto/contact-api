package com.github.contact.contact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.contact.contact.model.LoginUserRequest;
import com.github.contact.contact.model.TokenResponse;
import com.github.contact.contact.model.WebResponse;
import com.github.contact.contact.service.AuthService;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(
        path = "/api/auth/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginUserRequest request){
        
        TokenResponse response = authService.login(request);

        return WebResponse.<TokenResponse>builder().data(response).build();
    }
}
