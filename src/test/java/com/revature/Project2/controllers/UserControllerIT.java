package com.revature.Project2.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Project2.models.JsonResponse;
import com.revature.Project2.models.Post;
import com.revature.Project2.models.User;
import com.revature.Project2.services.PostService;
import com.revature.Project2.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(UserController.class)
class UserControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getOneUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void resetPassword() {
    }

    @Test
    void registerUser() throws Exception {
        User credentials = new User();
        credentials.setPassword("pass");
        credentials.setUserName("rmace12");
        credentials.setFirstName("richard");
        credentials.setLastName("mace");
        credentials.setEmail("rmace12@");

        List<Post> likes = new ArrayList<>();
        User user = new User(12, "richard", "mace", "rmace12",
                "rmace127@", null, "pass123", likes);
        JsonResponse expectedResult = new JsonResponse("user created", user);

        Mockito.when(this.userService.createUser(credentials)).thenReturn(user);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/user").param("firstName", credentials.getFirstName())
                .param("lastName", credentials.getLastName())
                .param("userName", credentials.getUserName())
                .param("email", credentials.getEmail())
                .param("password", credentials.getPassword())
                .contentType(MediaType.MULTIPART_FORM_DATA);


        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(new ObjectMapper().writeValueAsString(expectedResult)));


    }

    @Test
    void updateUser() {
    }

    @Test
    void likePost() {
    }
}