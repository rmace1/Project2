package com.revature.Project2.controllers;

import com.revature.Project2.models.User;
import com.revature.Project2.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserService userService = Mockito.mock(UserService.class);

    UserController userController;

    public UserControllerTest(){
        this.userController = new UserController(userService);
    }

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
    void registerUser() {
        User credentials = new User("richard", "mace", "rmace", "rmace@dotnet.com", "pass123");

        Mockito.when(userService.createUser(credentials)).thenReturn(credentials);

        User user = userService.createUser(credentials);

        assertEquals(credentials, user);
    }

    @Test
    void updateUser() {
    }

    @Test
    void likePost() {
    }
}