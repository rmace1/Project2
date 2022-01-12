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

    public List<User> getAllUsers(){
        return this.userRepo.findAll();
    }

    public User getOneUser(Integer userId){
        return this.userRepo.findById(userId).orElse(null);
    }

    public User getOneUserByUserName(String userName){return this.userRepo.findByUserName(userName);}

    public Boolean isUserNameAvailable(String userName){
        User user = this.userRepo.findByUserName(userName);
        if(user == null){
            return true;
        }
        return false;
    }

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
