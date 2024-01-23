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
import com.github.contact.contact.model.RegisterUserRequest;
import com.github.contact.contact.model.WebResponse;
import com.github.contact.contact.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
