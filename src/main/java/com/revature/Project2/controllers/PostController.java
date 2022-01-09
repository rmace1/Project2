package com.revature.Project2.controllers;

import com.revature.Project2.models.JsonResponse;
import com.revature.Project2.models.Post;
import com.revature.Project2.services.PostService;
import com.revature.Project2.services.UserService;
import com.revature.Project2.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

@RequestMapping(value = "post")
@RestController
@CrossOrigin(value = "http://localhost:4200")
public class PostController {
    private PostService postService;
    private UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService){
        this.postService = postService;
        this.userService = userService;
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
    public Post createPost(@RequestParam("file")MultipartFile file, @RequestParam String message,
                           @RequestParam("author") Integer authorId, @RequestParam(required = false) Integer originalPostId){
        Post post = new Post();

        post.setMessage(message);
        post.setAuthor(userService.getOneUser(authorId));
        if(originalPostId != null) {
            post.setOriginalPost(postService.getOnePost(originalPostId));
        }
        post.setTimePosted(new Timestamp(System.currentTimeMillis()));

        post.setPicture(FileUtil.uploadToS3(post.getAuthor(), file));
        return this.postService.createPost(post);
    }

    @GetMapping("{userId}/all-original-user")
    public List<Post> getAllPostsGivenUserId(@PathVariable Integer userId){
        return this.postService.getAllPostsGivenUserId(userId);
    }

    @GetMapping("all-original")
    public List<Post> getAllOriginalPosts(){
        return this.postService.getAllOriginalPosts();
    }

    @PutMapping
    public ResponseEntity<JsonResponse> updatePost(@RequestParam("file")MultipartFile file, @RequestParam String message,
           @RequestParam Integer postId){
        ResponseEntity responseEntity;
        Post post = new Post();

        post = postService.getOnePost(postId);
        post.setMessage(message);

        if(file != null) {
            post.setPicture(FileUtil.uploadToS3(post.getAuthor(), file));
        }
        Post updatedPost = postService.updatePost(post);
        return responseEntity = ResponseEntity.ok(new JsonResponse("Post with id " + updatedPost.getId() + " was updated", null));
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Integer id){
        ResponseEntity<String> responseEntity;
        if(this.postService.delete(id)){
            responseEntity = ResponseEntity.ok("Post with id " + id + " was deleted");
        }else{
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is not a post with id " + id);
        }
        return responseEntity;
    }
}
