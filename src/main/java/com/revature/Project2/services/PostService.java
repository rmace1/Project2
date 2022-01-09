package com.revature.Project2.services;

import com.revature.Project2.models.Post;
import com.revature.Project2.repository.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PostService {


    private PostRepo postRepo;

    @Autowired
    public PostService(PostRepo postRepo){
        this.postRepo = postRepo;
    }

    public Post createPost(Post post){
        return this.postRepo.save(post);
    }

    public List<Post> getAllPosts(){
        return this.postRepo.findAll();
    }

    public Post getOnePost(Integer postId){
        return this.postRepo.findById(postId).orElse(null);
    }

    public List<Post> getAllPostsGivenUserId(Integer userId){
        return this.postRepo.findAllUserByauthorId(userId);
    }

    public List<Post> getAllOriginalPosts(){
        return this.postRepo.findAllOriginalPosts();
    }

    public Post updatePost(Post post){

        return postRepo.save(post);
    }

    public Boolean deletePost(Integer postId){
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
