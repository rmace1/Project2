package com.revature.Project2.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Project2.models.JsonResponse;
import com.revature.Project2.models.Post;
import com.revature.Project2.models.User;
import com.revature.Project2.models.UserDTO;
import com.revature.Project2.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(SessionController.class)
class SessionControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void login() throws Exception {
        User credentials = new User();
        credentials.setUserName("rmace1");
        credentials.setPassword("pass123");

        List<Post> likes = new ArrayList<>();
        User user = new User(1, "richard", "mace", "rmace1",
                "rmace1@", null, "pass123", likes);
        UserDTO userDTO = new UserDTO(user);
        JsonResponse expectedResult = new JsonResponse("login successful", userDTO);

        Mockito.when(this.userService.validateCredentials(credentials)).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(credentials))
                .session(session))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(new ObjectMapper().writeValueAsString(expectedResult)));

        //assertEquals(userDTO, this.session.getAttribute("user-session"));

        //Test will fail if trying to use an object as the session value
        Mockito.verify(this.session, Mockito.times(1)).setAttribute("user-session", userDTO);
    }

    @Test
    void checkSession() {
    }

    @Test
    void logout() {
    }
}