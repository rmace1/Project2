package com.revature.Project2.services;

import com.revature.Project2.models.Post;
import com.revature.Project2.models.User;
import com.revature.Project2.repository.PostRepo;
import com.revature.Project2.repository.UserRepo;
import com.revature.Project2.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.web.multipart.MultipartFile;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the implementation for the methods in relation to the User model.
 */
@Service
@Transactional
public class UserService {
    private UserRepo userRepo;
    private PostRepo postRepo;

    @Autowired
    public UserService(UserRepo userRepo, PostRepo postRepo){
        this.userRepo = userRepo;
        this.postRepo = postRepo;
    }

    /**
     * Creates a new user with the provided information.
     * @param user The user object containing the information.
     * @return The newly created user object or null if the information is not valid.
     */
    public User createUser(User user){
        User userFromDb = this.userRepo.findByUserName(user.getUserName());
        if(userFromDb != null)
            return null;

        //http://www.jasypt.org/index.html
        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        String encryptedPass = encryptor.encryptPassword(user.getPassword());
        user.setPassword(encryptedPass);

        return this.userRepo.save(user);
    }

    /**
     * Returns the entire list of users from the database.
     * @return A list of all users.
     */
    public List<User> getAllUsers(){
        return this.userRepo.findAll();
    }

    /**
     * Returns a user object from the database given an id.
     * @param userId The id of the user object to be retrieved.
     * @return The user if they exist, will be null if they do not.
     */
    public User getOneUser(Integer userId){
        return this.userRepo.findById(userId).orElse(null);
    }

    /**
     * Returns a user object from the database given a username.
     * @param userName The username of the user object to be retrieved.
     * @return The user if they exist, will be null if they do not.
     */
    public User getOneUserByUserName(String userName){return this.userRepo.findByUserName(userName);}

    /**
     * Checks to see if a username is in use.
     * @param userName The name that is checked.
     * @return Returns true if the username is available for use.
     */
    public Boolean isUserNameAvailable(String userName){
        User user = this.userRepo.findByUserName(userName);
        if(user == null){
            return true;
        }
        return false;
    }

    /**
     * Updates the user's information in the database with what is passed, if no password or file is passed the existing
     * information will be used.
     * @param user The new user information to be saved to the database.
     * @param multipartFile The user profile picture they want to use.
     * @return
     */
    public User updateUser(User user, MultipartFile multipartFile){
        User userFromDb = userRepo.findById(user.getId()).orElse(null);

        if(userFromDb == null){
            return null;
        }

        //if a file is uploaded as part of the user update
        if(multipartFile != null){
            user.setProfilePic(FileUtil.uploadToS3(user, multipartFile));
        }else{
            user.setProfilePic(userFromDb.getProfilePic());
        }

        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        if(user.getPassword() != null) {
            String encryptedPass = encryptor.encryptPassword(user.getPassword());
            user.setPassword(encryptedPass);
        }else{
            user.setPassword(userFromDb.getPassword());
        }

        return userRepo.save(user);
    }

    /**
     * Deletes the user and all of their posts and liked posts from the database.
     * @param userId The id of the user to be deleted.
     * @return Returns true if the user no longer exists, false if the user object still remains in the database.
     */
    @Transactional
    public boolean delete(Integer userId){
        User user = this.userRepo.findById(userId).orElse(null);
        if(user == null){
            return true;
        }
        //removes the users liked posts from the join table
        user.setLikes(new ArrayList<Post>());
        userRepo.save(user);

        //deletes the user's posts and comments
        for(Post post: postRepo.findAllPostsByUser(user.getId())){
            postRepo.deleteById(post.getId());
        }

        //delete the user
        this.userRepo.deleteById(userId);
        user = this.userRepo.findById(userId).orElse(null);
        if(user == null) {
            return true;
        }
        return false;
    }

    /**
     * Validates that the passed user credentials match what exist in the database.
     * @param user The passed user credentials.
     * @return The user object from the database that has matching username/password combination.
     */
    public User validateCredentials(User user){
        User userFromDb = this.userRepo.findByUserName(user.getUserName());

        if(userFromDb == null)
            return null;

        StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
        //checks to see if password matches encrypted password.
        if(!encryptor.checkPassword(user.getPassword(), userFromDb.getPassword())) {
            return null;
        }
        return userFromDb;
    }

    /**
     * Adds the post to the list of user's liked posts.
     * @param userId The id of the user who liked the post.
     * @param postId The id of the post that is liked.
     * @return Will return true if the post is added to the table and its number of likes is incremented successfully,
     *  will return false if the post has already been liked.
     */
    @Transactional
    public Boolean addLike(Integer userId, Integer postId){
        User user = getOneUser(userId);
        Post post = postRepo.getById(postId);

        //if the user has not already liked the post
        if(!user.getLikes().contains(post)) {
            //adds the user/post combination to the user_post table
            user.getLikes().add(post);
            userRepo.save(user);

            //increments the likes value by 1
            post.setLikes(post.getLikes() + 1);
            postRepo.save(post);

            return true;
        }
        return false;
    }
}
