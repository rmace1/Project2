package com.revature.Project2.controllers;

import com.revature.Project2.models.User;
import com.revature.Project2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "user")
@RestController
@CrossOrigin(value = "http://localhost:9000")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers(){
        return this.userService.getAllUsers();
    }

    @GetMapping("{userId}")
    public User getOneUser(@PathVariable Integer userId){
        return this.userService.getOneUser(userId);
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return this.userService.createUser(user);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id){
        ResponseEntity<String> responseEntity;

        if(this.userService.delete(id)){
            responseEntity = ResponseEntity.ok("User with id " + id + " was deleted");
        }else{
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is not a user with id " + id);
        }

        return responseEntity;
    }
}
