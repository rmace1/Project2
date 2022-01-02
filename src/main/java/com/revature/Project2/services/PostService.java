package com.revature.Project2.services;

import com.revature.Project2.models.Post;
import com.revature.Project2.repository.PostRepo;
import com.revature.Project2.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepo postRepo;

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
        return this.postRepo.findAllByUserId(userId);
    }

    public List<Post> getAllPostsGivenPostId(Integer postId){
        return this.postRepo.findAllByPostId(postId);
    }
}
