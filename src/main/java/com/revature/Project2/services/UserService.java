package com.revature.Project2.services;

import com.revature.Project2.models.User;
import com.revature.Project2.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User createUser(User user){
        return this.userRepo.save(user);
    }

    public List<User> getAllUsers(){
        return this.userRepo.findAll();
    }

    public User getOneUser(Integer userId){
        return this.userRepo.findById(userId).orElse(null);
    }

    public boolean delete(Integer userId){
        this.userRepo.deleteById(userId);
        if(this.userRepo.findById(userId).equals(null))
            return true;
        return false;
    }
}