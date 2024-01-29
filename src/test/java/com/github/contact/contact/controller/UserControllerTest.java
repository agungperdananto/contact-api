package com.github.contact.contact.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.contact.contact.entity.User;
import com.github.contact.contact.model.RegisterUserRequest;
import com.github.contact.contact.model.UserResponse;
import com.github.contact.contact.model.WebResponse;
import com.github.contact.contact.repository.UserRepository;
import com.github.contact.contact.security.BCrypt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }



    @Test
    void testRegisterSuccess() throws Exception{
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("root");
        request.setPassword("123");
        request.setName("root user");

        mockMvc.perform(
            post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON) )
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
        ).andExpectAll(
            status().isOk()
        ).andDo(
            result->{
                WebResponse<String> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

                assertEquals("OK", response.getData());
            });

    }

    @Test
    void testRegisterDuplicate() throws Exception{
        User user = new User();

        user.setUsername("root");
        user.setPassword(BCrypt.hashpw("123", BCrypt.gensalt()));
        user.setName("root user");

        userRepository.save(user);

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("root");
        request.setPassword("123");
        request.setName("root user");

        mockMvc.perform(
            post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON) )
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
        ).andExpectAll(
            status().isBadRequest()
        ).andDo(
            result->{
                WebResponse<String> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

                assertNotNull(response.getErrors());
            });

    }

    @Test
    void testRegisterFailed() throws Exception{
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");
        request.setName("");

        mockMvc.perform(
            post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON) )
                        .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
        ).andExpectAll(
            status().isBadRequest()
        ).andDo(
            result->{
                WebResponse<String> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

                assertNotNull(response.getErrors());
            });

    }

    @Test
    void getUserUnautorized() throws Exception{
        mockMvc.perform(
            get("/api/users/current")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "notfound")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(
            result->{
                WebResponse<String> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

                assertNotNull(response.getErrors());
        });
    }

    @Test
    void getUserUnautorizedEmptyToken() throws Exception{
        mockMvc.perform(
            get("/api/users/current")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(
            result->{
                WebResponse<String> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

                assertNotNull(response.getErrors());
        });
    }

    @Test
    void getUserUnautorizedExpiredToken() throws Exception{
        User user = new User();

        user.setUsername("root");
        user.setPassword(BCrypt.hashpw("123", BCrypt.gensalt()));
        user.setToken("x-12345");
        user.setName("root user");
        user.setTokenExpiredAt(System.currentTimeMillis() - 100000L);

        userRepository.save(user);
    
        mockMvc.perform(
            get("/api/users/current")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(
            result->{
                WebResponse<String> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

                assertNotNull(response.getErrors());
        });
    }
    
   
    @Test
    void getUserSuccess() throws Exception{
        User user = new User();

        user.setUsername("root");
        user.setPassword(BCrypt.hashpw("123", BCrypt.gensalt()));
        user.setToken("x-12345");
        user.setName("root user");
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000L);

        userRepository.save(user);

        mockMvc.perform(
            get("/api/users/current")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "x-12345")
        ).andExpectAll(
            status().isOk()
        ).andDo(
            result->{
                WebResponse<UserResponse> response =  objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>(){});

                assertNull(response.getErrors());
                assertEquals("root", response.getData().getUsername());
                assertEquals("root user", response.getData().getName());
        });
    }
}
