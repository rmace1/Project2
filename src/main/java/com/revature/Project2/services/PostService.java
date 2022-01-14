package com.revature.Project2.services;

import com.revature.Project2.models.Post;
import com.revature.Project2.repository.PostRepo;
import com.revature.Project2.repository.PostRepoPaged;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Contains the implementation for the methods in relation to the Post model.
 */
@Service
public class PostService {
    private Logger log = Logger.getLogger(PostService.class);

    private PostRepo postRepo;
    private PostRepoPaged postRepoPaged;

    @Autowired
    public PostService(PostRepo postRepo, PostRepoPaged postRepoPaged){
        this.postRepo = postRepo;
        this.postRepoPaged = postRepoPaged;
    }

    /**
     * Creates a new post from the given post.
     * @param post The given information about a post.
     * @return The created post.
     */
    public Post createPost(Post post){
        log.info("Post created: " + post.toString());
        return this.postRepo.save(post);
    }

    /**
     * Retrieves all posts from the database.
     * @return A list containing all posts.
     */
    public List<Post> getAllPosts(){
        return this.postRepo.findAll();
    }

    /**
     * Returns a single post given an id.
     * @param postId The id of the post to be returned.
     * @return The post object, if it exists, from the database.
     */
    public Post getOnePost(Integer postId){
        return this.postRepo.findById(postId).orElse(null);
    }

    /**
     * Retrieves the list of posts that a given user has made, not including comments.
     * @param userId The id of the user that made posts.
     * @return A list of posts.
     */
    public List<Post> getAllPostsGivenUserId(Integer userId){
        List<Post> posts = this.postRepo.findAllPostsByUser(userId);
        for (Post post: posts) {
            Collections.sort(post.getComments());
        }
        return posts;
    }

    /**
     * Retrieves all posts that are not comments.
     * @return A list of posts that are not comments.
     */
    public List<Post> getAllOriginalPosts(){
        List<Post> posts = this.postRepo.findAllOriginalPosts();
        for (Post post: posts) {
            Collections.sort(post.getComments());
        }
        return posts;
    }

    /**
     * Updates the post information in the database.
     * @param post The post information to update.
     * @return Returns the updated post.
     */
    public Post updatePost(Post post){
        log.info("Post updated: " + post.toString());
        return postRepo.save(post);
    }

    /**
     * Deletes a post given an id.
     * @param postId The id of the post to delete.
     * @return Returns true if the deletion was successful.
     */
    public boolean delete(Integer postId){
        this.postRepo.deleteById(postId);
        Post post = this.postRepo.findById(postId).orElse(null);

        if(post == null) {
            return true;
        }

        return false;
    }

    /**
     * Returns the list of posts that have been divided into pages.
     * @param pageNo The page number to return.
     * @param pageSize The number of objects on each page.
     * @return A list of posts.
     */
    public List<Post> findAllPostsPaginated(int pageNo, int pageSize){
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<Post> pagedResult = this.postRepoPaged.findAllOriginalPostsPaged(paging);

        if(pagedResult.isEmpty() && pageNo >= 9999){
            return getAllOriginalPosts();
        }
        return pagedResult.toList();

    }

}
