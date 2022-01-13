package com.revature.Project2.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.Project2.models.JsonResponse;
import com.revature.Project2.models.Post;
import com.revature.Project2.models.User;
import com.revature.Project2.models.UserDTO;
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
import java.util.Optional;

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
    void getAllUsers() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User("richard", "mace", "rmace", "rmace@net.com", "pass"));
        users.add(new User("richard", "mace", "rmace2", "rmace2@net.com", "pass123"));
        users.get(0).setId(1);
        users.get(1).setId(2);


        Mockito.when(userService.getAllUsers()).thenReturn(users);

        RequestBuilder request = MockMvcRequestBuilders.get("/user");

        mvc.perform(request).andExpect(MockMvcResultMatchers.content()
                .json(new ObjectMapper().writeValueAsString(users)));
    }

    @Test
    void getOneUser() throws Exception {

        User user = new User("richard", "mace", "rmace", "rmace@net.com", "pass");
        user.setId(1);
        UserDTO userDTO = new UserDTO(user);

        Mockito.when(userService.getOneUser(user.getId())).thenReturn(user);

        RequestBuilder request = MockMvcRequestBuilders.get("/user/" + user.getId());

        mvc.perform(request).andExpect(MockMvcResultMatchers.content()
                        .json(new ObjectMapper().writeValueAsString(userDTO)));
    }

    @Test
    void deleteValidUser() throws Exception {

        Mockito.when(userService.delete(1)).thenReturn(true);

        RequestBuilder request = MockMvcRequestBuilders.delete("/user/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                .json(new ObjectMapper().writeValueAsString(new JsonResponse("User with id 1 was deleted", null))));

    }

    @Test
    void deleteInvalidUser() throws Exception {
        Mockito.when(userService.delete(1)).thenReturn(false);

        RequestBuilder request = MockMvcRequestBuilders.delete("/user/{id}", 1).param("id", "1")
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper()
                        .writeValueAsString(new JsonResponse("There is not a user with id 1", null))));

    }

    @Test
    void resetPassword() throws Exception {
        User credentials = new User("richard", "mace", "rmace", "rmace@dotnet.com", "pass");
        credentials.setId(1);

        User user = new User("richard", "mace", "rmace", "rmace@dotnet.com", "secretPass123");
        user.setId(1);

        UserDTO userDTO = new UserDTO(user);

        Mockito.when(userService.getOneUserByUserName(credentials.getUserName())).thenReturn(credentials);
        Mockito.when(userService.updateUser(credentials, null)).thenReturn(user);

        RequestBuilder request = MockMvcRequestBuilders.patch("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(credentials));

        mvc.perform(request).andExpect(MockMvcResultMatchers.content().json(new ObjectMapper()
                        .writeValueAsString(new JsonResponse("Password reset.", userDTO))));
    }

    @Test
    void registerUser() throws Exception {
        User newUser = new User();
        newUser.setPassword("pass");
        newUser.setUserName("rmace12");
        newUser.setFirstName("richard");
        newUser.setLastName("mace");
        newUser.setEmail("rmace12@");
        newUser.setProfilePic("https://jwa-p2.s3.us-east-2.amazonaws.com/SocialNetwork/avatar-default-square.jpg");

        List<Post> likes = new ArrayList<>();
        User user = new User(1, "richard", "mace", "rmace12",
                "rmace127@", null, "pass123", likes, likes);
        JsonResponse expectedResult = new JsonResponse("user created", user);

        Mockito.when(this.userService.createUser(newUser)).thenReturn(user);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/user").param("firstName", newUser.getFirstName())
                .param("lastName", newUser.getLastName())
                .param("userName", newUser.getUserName())
                .param("email", newUser.getEmail())
                .param("password", newUser.getPassword())
                .contentType(MediaType.MULTIPART_FORM_DATA);


        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(new ObjectMapper().writeValueAsString(expectedResult)));


    }

    @Test
    void updateUser() throws Exception {
        User credentials = new User();
        credentials.setId(1);
        credentials.setPassword("");
        credentials.setUserName("rmace12");
        credentials.setFirstName("richard");
        credentials.setLastName("mace");
        credentials.setEmail("rmace12@");

        List<Post> likes = new ArrayList<>();
        User user = new User(1, "richard", "mace", "rmace12",
                "rmace12@", null, "pass123", likes, likes);

        Mockito.when(userService.getOneUser(credentials.getId())).thenReturn(user);
        Mockito.when(userService.updateUser(credentials, null)).thenReturn(user);

        RequestBuilder request = MockMvcRequestBuilders
                .put("/user").param("firstName", credentials.getFirstName())
                .param("id", credentials.getId().toString())
                .param("lastName", credentials.getLastName())
                .param("userName", credentials.getUserName())
                .param("email", credentials.getEmail())
                .param("password", credentials.getPassword())
                .contentType(MediaType.MULTIPART_FORM_DATA);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper()
                        .writeValueAsString(new JsonResponse("user updated", user))));

    }

    @Test
    void likePostSuccessfully() throws Exception {

        Mockito.when(userService.addLike(1,2)).thenReturn(true);

        RequestBuilder request = MockMvcRequestBuilders.patch("/user/{userId}/post/{postId}",1,2);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper()
                        .writeValueAsString(new JsonResponse("Post liked successfully.", null))));

    }

    @Test
    void likePostFailure() throws Exception {

        Mockito.when(userService.addLike(1,2)).thenReturn(false);

        RequestBuilder request = MockMvcRequestBuilders.patch("/user/{userId}/post/{postId}",1,2);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper()
                        .writeValueAsString(new JsonResponse("Post already liked.", null))));

    }
}