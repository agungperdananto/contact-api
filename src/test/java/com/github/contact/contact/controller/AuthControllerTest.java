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
import com.github.contact.contact.model.LoginUserRequest;
import com.github.contact.contact.model.TokenResponse;
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
public class AuthControllerTest {

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
    void testLoginSucess() throws Exception{
        User user = new User();

        user.setUsername("root");
        user.setPassword(BCrypt.hashpw("123", BCrypt.gensalt()));
        user.setName("root user");

        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("root");
        request.setPassword("123");

        mockMvc.perform(
            post("/api/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
        ).andExpectAll(
            status().isOk()
        ).andDo(
            result ->{
                WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    
                });
                assertNull(response.getErrors());
                assertNotNull(response.getData().getToken());
                assertNotNull(response.getData().getExpiredAt());
                
                User userTest = userRepository.findById(Objects.requireNonNull(request.getUsername())).orElse(null);

                assertNotNull(userTest);
                assertEquals(userTest.getToken(), response.getData().getToken());
                assertEquals(userTest.getTokenExpiredAt(), response.getData().getExpiredAt());
            }
        );

    }
    

    @Test
    void testLoginFailedUserNotFound() throws Exception{

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("root");
        request.setPassword("123");

        mockMvc.perform(
            post("/api/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(
            result ->{
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    
                });
                assertNotNull(response.getErrors());
            }
        );
    }

    @Test
    void testLoginFailedInvalidPassword() throws Exception{

        User user = new User();

        user.setUsername("root");
        user.setPassword(BCrypt.hashpw("1234", BCrypt.gensalt()));
        user.setName("root user");

        userRepository.save(user);

        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("root");
        request.setPassword("123");

        mockMvc.perform(
            post("/api/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_JSON))
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(request)))
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(
            result ->{
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    
                });
                assertNotNull(response.getErrors());
            }
        );
    }

    @Test
    void logoutFailed() throws Exception{
        mockMvc.perform(
            delete("/api/auth/logout")
                .accept(MediaType.APPLICATION_JSON)  
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(
            result ->{
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });
                assertNotNull(response.getErrors());
            }
        );
    }

    @Test
    void logoutSuccess() throws Exception{
        User user = new User();

        user.setUsername("admin");
        user.setPassword(BCrypt.hashpw("123", BCrypt.gensalt()));
        user.setToken("x-123456");
        user.setName("root user");
        user.setTokenExpiredAt(System.currentTimeMillis() + 100000L);
        userRepository.save(user);

        mockMvc.perform(
            delete("/api/auth/logout")
                .accept(MediaType.APPLICATION_JSON)  
                .header("X-API-TOKEN", "x-123456")
        ).andExpectAll(
            status().isOk()
        ).andDo(
            result ->{
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                });
                assertNull(response.getErrors());

                assertEquals("OK", response.getData());

                User userObj = userRepository.findById("admin").orElse(null);

                assertNotNull(userObj);
                assertNull(userObj.getToken());
                assertNull(userObj.getTokenExpiredAt());

            }
        );
    }
}
