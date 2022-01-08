package com.revature.Project2.controllers;

import com.revature.Project2.models.JsonResponse;
import com.revature.Project2.models.User;
import com.revature.Project2.models.UserDTO;
import com.revature.Project2.services.UserService;
import com.revature.Project2.util.Email;
import com.revature.Project2.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping(value = "user")
@RestController
@CrossOrigin(value = "http://localhost:4200")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("{userId}")
    public User getOneUser(@PathVariable Integer userId) {
        return this.userService.getOneUser(userId);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<JsonResponse> deleteUser(@PathVariable Integer id) {
        ResponseEntity<JsonResponse> responseEntity;

        if (this.userService.delete(id)) {
            responseEntity = ResponseEntity
                    .ok(new JsonResponse("User with id " + id + " was deleted", null));
        } else {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new JsonResponse("There is not a user with id " + id, null));
        }

        return responseEntity;
    }

    @PatchMapping
    public ResponseEntity<JsonResponse> resetPassword(@RequestParam("file") MultipartFile file, @RequestParam String firstName, @RequestParam String lastName
            , @RequestParam String userName, @RequestParam String email, @RequestParam String password) {

        //TODO: upload and save profile image to DB and S3.
        ResponseEntity<JsonResponse> responseEntity;
        //TODO: add password generator potentially
        String newPass = "P4ssw0rd";
        //check if user is valid
        User newUser = new User(firstName, lastName, userName, email, password);

        User user = userService.validateCredentials(newUser);

        //if not return json response
        if (user == null) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new JsonResponse("No user found.", null));
            return responseEntity;
        }

        //if so reset password to p4ssw0rd
        user.setPassword(newPass);

        //reset password and send email
        userService.updateUser(user);
        Email.sendEmail(user.getEmail(), "Password Reset",
                "Your password has been reset to \"" + newPass + "\"");
        UserDTO userDto = new UserDTO(user);
        //return json response
        responseEntity = ResponseEntity.ok(new JsonResponse("Password reset.", userDto));
        return responseEntity;
    }

    @PostMapping
    public ResponseEntity<JsonResponse> registerUser(@RequestParam("file") MultipartFile file, @RequestParam String firstName, @RequestParam String lastName
            , @RequestParam String userName, @RequestParam String email, @RequestParam String password) {

        User newUser = new User(firstName, lastName, userName, email, password);

        //TODO: make sure image location is saved to DB
        //newUser.setProfileImage(FileUtil.convertToFile(file));
        newUser.setProfilePic(FileUtil.uploadToS3(newUser, file));

        User user = this.userService.createUser(newUser);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponse("username already exists in system", null));
        }
        return ResponseEntity.ok(new JsonResponse("user created", user));
    }

    //TODO: Add endpoint to add a post to the user's likes list
}