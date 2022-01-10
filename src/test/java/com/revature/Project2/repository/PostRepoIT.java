package com.revature.Project2.repository;

import com.revature.Project2.models.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostRepoIT {

    @Autowired
    PostRepo postRepo;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAllPostsByUser() {
        List<Post> posts = new ArrayList<>();

        posts = postRepo.findAllPostsByUser(1);

        assertEquals(2, posts.size());
    }

    @Test
    void findAllOriginalPosts() {
        List<Post> posts = new ArrayList<>();

        posts = postRepo.findAllOriginalPosts();

        assertEquals(5, posts.size());
    }
}