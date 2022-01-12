package com.revature.Project2.services;

import com.revature.Project2.models.Post;
import com.revature.Project2.models.User;
import com.revature.Project2.repository.PostRepo;
import com.revature.Project2.repository.UserRepo;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    UserService userService;
    UserRepo userRepo = Mockito.mock(UserRepo.class);
    PostRepo postRepo = Mockito.mock(PostRepo.class);

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userRepo, postRepo);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createValidUser() {
        User expectedResult = new User("rmace", "richard", "mace", "rmace@rev.net", "Pass123");

        Mockito.when(userRepo.findByUserName(expectedResult.getUserName())).thenReturn(null);
        Mockito.when(userRepo.save(expectedResult)).thenReturn(expectedResult);

        User actualResult = userService.createUser(expectedResult);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void createInvalidUser() {
        User expectedResult = new User("rmace", "richard", "mace", "rmace@rev.net", "Pass123");

        Mockito.when(userRepo.findByUserName(expectedResult.getUserName())).thenReturn(expectedResult);
        Mockito.when(userRepo.save(expectedResult)).thenReturn(expectedResult);

        User actualResult = userService.createUser(expectedResult);

        assertNull(actualResult);
        Mockito.verify(userRepo, Mockito.times(0)).save(expectedResult);
    }

    @Test
    void encryptsPassword(){
        String password = "P4ssw0rd";
        User expectedResult = new User("rmace", "richard", "mace", "rmace@rev.net", password);

        Mockito.when(userRepo.findByUserName(expectedResult.getUserName())).thenReturn(null);
        Mockito.when(userRepo.save(expectedResult)).thenReturn(expectedResult);

        User actualResult = userService.createUser(expectedResult);

        assertNotEquals(password, actualResult.getPassword());
    }

    @Test
    void getAllUsers() {
        List<User> users = new ArrayList<>();

        users.add(userService.createUser(new User("rmace", "richard", "mace", "rmace@rev.net", "Pass123")));
        users.add(userService.createUser(new User("kchild", "kevin", "childs", "kchild@rev.net", "p4ssw0rd")));
        users.add(userService.createUser(new User("ebaier", "eian", "baier", "ebaier@rev.net", "Password1")));

        Mockito.when(userRepo.findAll()).thenReturn(users);

        List<User> actual = userService.getAllUsers();

        assertEquals(users, actual);
    }

    @Test
    void getOneUser() {
        User expectedResult = new User("rmace", "richard", "mace", "rmace@rev.net", "Pass123");

        Mockito.when(userRepo.findById(expectedResult.getId())).thenReturn(Optional.of(expectedResult));

        User actual = userService.getOneUser(expectedResult.getId());

        assertEquals(expectedResult, actual);
    }

    @Test
    void updateUser() {
        List<Post> likes = new ArrayList<>();
        User expectedResult = new User(2, "richard", "mace", "rmace2",
                "rmace2@", null, "pass", likes, likes);
        Mockito.when(userRepo.findById(expectedResult.getId())).thenReturn(Optional.of(expectedResult));
        Mockito.when(userRepo.save(expectedResult)).thenReturn(expectedResult);

        User actual = userService.updateUser(expectedResult, null);

        assertEquals(expectedResult, actual);
    }

    @Test
    void InvalidDelete() {
        User expectedResult = new User("rmace", "richard", "mace", "rmace@rev.net", "Pass123");
        expectedResult.setId(0);

        Mockito.when(userRepo.findById(expectedResult.getId())).thenReturn(Optional.of(expectedResult));

        Boolean deleted = userService.delete(expectedResult.getId());

        assertFalse(deleted);

    }

    @Test
    void validDelete() {
        User expectedResult = new User("rmace", "richard", "mace", "rmace@rev.net", "Pass123");
        expectedResult.setId(1);

        Mockito.when(userRepo.findById(expectedResult.getId())).thenReturn(Optional.ofNullable(null));

        Boolean deleted = userService.delete(expectedResult.getId());

        assertTrue(deleted);
    }

    @Test
    void validateValidCredentials() {
        User user = new User("rmace", "richard", "mace", "rmace@rev.net", "Pass123");
        User dbUser = new User("rmace", "richard", "mace", "rmace@rev.net", "Pass123");
        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        dbUser.setPassword(encryptor.encryptPassword(dbUser.getPassword()));


        Mockito.when(userRepo.findByUserName(user.getUserName())).thenReturn(dbUser);

        User actual = userService.validateCredentials(user);

        assertEquals(dbUser, actual);
    }

    @Test
    void validateInvalidCredentials() {
        User user = new User("rmace", "richard", "mace", "rmace@rev.net", "Pass123");
        User dbUser = new User("rmacce", "richard", "mace", "rmace@rev.net", "Pass123");
        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        dbUser.setPassword(encryptor.encryptPassword(dbUser.getPassword()));


        Mockito.when(userRepo.findByUserName(user.getUserName())).thenReturn(null);

        User actual = userService.validateCredentials(user);

        assertNull(actual);
    }

    @Test
    void validateInvalidPasswordCredentials() {
        User user = new User("rmace", "richard", "mace", "rmace@rev.net", "Pass122");
        User dbUser = new User("rmace", "richard", "mace", "rmace@rev.net", "Pass123");
        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        dbUser.setPassword(encryptor.encryptPassword(dbUser.getPassword()));


        Mockito.when(userRepo.findByUserName(user.getUserName())).thenReturn(dbUser);

        User actual = userService.validateCredentials(user);

        assertNull(actual);
    }
}