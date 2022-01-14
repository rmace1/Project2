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

/**
 * Handles the enpdpoint logic for the Post object.
 */
@RequestMapping(value = "post")
@RestController
@CrossOrigin(value = "http://3.21.168.108:4200")
public class PostController {
    private PostService postService;
    private UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService){
        this.postService = postService;
        this.userService = userService;
    }

    /**
     * Retrieves a list of all posts.
     * @return A list of posts.
     */
    @GetMapping
    public List<Post> getAllPosts(){
        return this.postService.getAllPosts();
    }

    /**
     * Retrieves a post by its id.
     * @param postId The id of the post.
     * @return The retrieved post.
     */
    @GetMapping("{postId}")
    public Post getOnePost(@PathVariable Integer postId){
        return this.postService.getOnePost(postId);
    }

    /**
     * Creates a post from the given information.
     * @param file The file attached to the post, optional.
     * @param message The post's message.
     * @param authorId The id of the user who made the post.
     * @param originalPostId The id of the post this is commenting on, optional.
     * @return The created Post
     */
    @PostMapping
    public Post createPost(@RequestParam(value = "file", required = false)MultipartFile file, @RequestParam String message,
                           @RequestParam("author") Integer authorId, @RequestParam(required = false) Integer originalPostId){
        Post post = new Post();
        post.setLikes(0);
        post.setMessage(message);
        post.setAuthor(userService.getOneUser(authorId));
        if(originalPostId != null) {
            post.setOriginalPost(postService.getOnePost(originalPostId));
        }
        post.setTimePosted(new Timestamp(System.currentTimeMillis()));

        if(file != null) {
            post.setPicture(FileUtil.uploadToS3(post.getAuthor(), file));
        }
        return this.postService.createPost(post);
    }

    /**
     * Retrieves the list of posts by a given user that are not comments.
     * @param userId The user
     * @return
     */
    @GetMapping("{userId}/all-original-user")
    public List<Post> getAllPostsGivenUserId(@PathVariable Integer userId){
        return this.postService.getAllPostsGivenUserId(userId);
    }

    /**
     * Retrieves the list of all posts that are not comments.
     * @return A list of posts
     */
    @GetMapping("all-original")
    public List<Post> getAllOriginalPosts(){
        return this.postService.getAllOriginalPosts();
    }

    /**
     * Updates a post with the provided information.
     * @param file The file attached to the post.
     * @param message The message of the post.
     * @param postId The id of the post.
     * @return The JsonResponse detailing the update process.
     */
    @PutMapping
    public ResponseEntity<JsonResponse> updatePost(@RequestParam(value = "file", required = false)MultipartFile file, @RequestParam String message,
           @RequestParam Integer postId){
        Post post = new Post();

        post = postService.getOnePost(postId);
        post.setMessage(message);

        if(file != null) {
            post.setPicture(FileUtil.uploadToS3(post.getAuthor(), file));
        }
        Post updatedPost = postService.updatePost(post);
        return ResponseEntity.ok(new JsonResponse("Post with id " + updatedPost.getId() + " was updated", null));
    }

    /**
     * The endpoint for deleting a post.
     * @param id The id of the post to delete.
     * @return A string detailing if the post was deleted.
     */
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

    /**
     * The endpoint for retrieving the paged list of posts
     * @param pageNo The page number to retrieve.
     * @param pageSize The number of posts on each page.
     * @return The list of posts of the predetermined size.
     */
    @GetMapping("/paged/{pageNo}/{pageSize}")
    public List<Post> getPaginatedPosts(@PathVariable Integer pageNo
            , @PathVariable Integer pageSize){
        return this.postService.findAllPostsPaginated(pageNo, pageSize);
    }
}
