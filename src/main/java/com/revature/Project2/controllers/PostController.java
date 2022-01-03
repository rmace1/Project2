package com.revature.Project2.controllers;

import com.revature.Project2.models.Post;
import com.revature.Project2.models.User;
import com.revature.Project2.services.PostService;
import com.revature.Project2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "post")
@RestController
@CrossOrigin(value = "http://localhost:4200")
public class PostController {
    private PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping
    public List<Post> getAllPosts(){
        return this.postService.getAllPosts();
    }

    @GetMapping("{postId}")
    public Post getOnePost(@PathVariable Integer postId){
        return this.postService.getOnePost(postId);
    }

    @PostMapping
    public Post createPost(@RequestBody Post post){return this.postService.createPost(post);}

    @GetMapping("alluser/{userId}")
    public List<Post> getAllPostsGivenUserId(@PathVariable Integer userId){
        return this.postService.getAllPostsGivenUserId(userId);
    }

    @GetMapping("allpost/{postId}")
    public List<Post> getAllPostsGivenPostId(@PathVariable Integer postId){
        return this.postService.getAllPostsGivenPostId(postId);
    }
}
