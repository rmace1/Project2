package com.revature.Project2.repository;

import com.revature.Project2.models.Post;
import com.revature.Project2.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepoIT {

    @Autowired
    UserRepo userRepo;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByUserName() {
        //If you test a user that has any likes a stack overflow occurs due to an infinite loop between the objects.
        List<Post> likes = new ArrayList<>();
        User expected = new User(2, "richard", "mace", "rmace2",
                "rmace2@", null, "pass",likes, likes);

        User actual = userRepo.findByUserName("rmace2");

        assertEquals(expected.toString(), actual.toString());
    }
}