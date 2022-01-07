package com.revature.Project2.services;

import com.revature.Project2.models.Post;
import com.revature.Project2.models.User;
import com.revature.Project2.repository.PostRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PostServiceTest {
    PostService postService;
    PostRepo postRepo = Mockito.mock(PostRepo.class);

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepo);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createPost() {
        User user = new User("Richard", "Mace", "rmace", "rmace@revnet.com", "pass123");
        Post post = new Post(1, "First post!", user);

        Mockito.when(postRepo.save(post)).thenReturn(post);

        Post actual = postService.createPost(post);

        assertEquals(post, actual);
    }

    @Test
    void getAllPosts() {
        List<Post> posts = new ArrayList<>();
        User user = new User();
        posts.add(new Post(1, "First post!", user));
        posts.add(new Post(2, "Second post!", user));
        posts.add(new Post(3, "Third post!", user));

        Mockito.when(postRepo.findAll()).thenReturn(posts);

        List<Post> actual = postService.getAllPosts();

        assertEquals(posts, actual);
    }

    @Test   //TODO: do negative test
    void getOnePost() {
        User user = new User("Richard", "Mace", "rmace", "rmace@revnet.com", "pass123");
        Post post = new Post(1, "First post!", user);

        Mockito.when(postRepo.findById(post.getId())).thenReturn(Optional.of(post));

        Post actual = postService.getOnePost(post.getId());

        assertEquals(post, actual);
    }

    @Test
    void getAllPostsGivenUserId() {
        User user = new User("Richard", "Mace", "rmace", "rmace@revnet.com", "pass123");
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1, "First post!", user));
        posts.add(new Post(2, "Second post!", user));
        posts.add(new Post(3, "Third post!", user));

        Mockito.when(postRepo.findAllUserByauthorId(user.getId())).thenReturn(posts);

        List<Post> actual = postService.getAllPostsGivenUserId(user.getId());

        assertEquals(posts, actual);
    }

    @Test
    void getAllOriginalPosts() {
        User user = new User("Richard", "Mace", "rmace", "rmace@revnet.com", "pass123");
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1, "First post!", user));
        posts.add(new Post(2, "Second post!", user));
        posts.add(new Post(3, "Third post!", user));

        Mockito.when(postRepo.findAllOriginalPosts()).thenReturn(posts);
    }

    @Test
    void updatePost() {
        User user = new User("Richard", "Mace", "rmace", "rmace@revnet.com", "pass123");
        Post post = new Post(1, "First post!", user);

        Mockito.when(postRepo.save(post)).thenReturn(post);

        Post actual = postRepo.save(post);

        assertEquals(post, actual);
    }

    @Test
    void deletePost() {
        User user = new User("Richard", "Mace", "rmace", "rmace@revnet.com", "pass123");
        Post post = new Post(1, "First post!", user);

        Boolean deleted = postService.deletePost(post.getId());

        assertTrue(deleted);

    }
}