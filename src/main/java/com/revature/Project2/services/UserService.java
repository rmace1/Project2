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

    public User updateUser(User user, MultipartFile multipartFile){
        User userFromDb = userRepo.findByUserName(user.getUserName());

        if(userFromDb == null){
            return null;
        }

        //if a file is uploaded as part of the user update
        if(multipartFile != null){
            user.setProfilePic(FileUtil.uploadToS3(user, multipartFile));
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

    public boolean delete(Integer userId){
        this.userRepo.deleteById(userId);
        User user = this.userRepo.findById(userId).orElse(null);
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
    public void addLike(User user, Post post){
        if(!user.getLikes().contains(post)) {
            user.getLikes().add(post);
            userRepo.save(user);
            post.setLikes(post.getLikes() + 1);
            postRepo.save(post);
        }
    }
}
