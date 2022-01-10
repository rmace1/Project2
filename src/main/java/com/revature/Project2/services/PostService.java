package com.revature.Project2.services;

import com.revature.Project2.models.Post;
import com.revature.Project2.repository.PostRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PostService {
    private Logger log = Logger.getLogger(PostService.class);

    private PostRepo postRepo;

    @Autowired
    public PostService(PostRepo postRepo){
        this.postRepo = postRepo;
    }

    public Post createPost(Post post){
        log.info("Post created: " + post.toString());
        return this.postRepo.save(post);
    }

    public List<Post> getAllPosts(){
        return this.postRepo.findAll();
    }

    public Post getOnePost(Integer postId){
        return this.postRepo.findById(postId).orElse(null);
    }

    //sorts the comments on each post
    public List<Post> getAllPostsGivenUserId(Integer userId){
        List<Post> posts = this.postRepo.findAllPostsByUser(userId);
        for (Post post: posts) {
            Collections.sort(post.getComments());
        }
        return posts;
    }

    //sorts the comments on each post
    public List<Post> getAllOriginalPosts(){
        List<Post> posts = this.postRepo.findAllOriginalPosts();
        for (Post post: posts) {
            Collections.sort(post.getComments());
        }
        return posts;
    }

    public Post updatePost(Post post){
        log.info("Post updated: " + post.toString());
        return postRepo.save(post);
    }

    public Boolean deletePost(Integer postId){
        log.info("Post deleted: " + postId);
        postRepo.deleteById(postId);
        return true;
    }

    public boolean delete(Integer postId){
        this.postRepo.deleteById(postId);
        if(this.postRepo.findById(postId).equals(null))
            return true;
        return false;
    }
}
