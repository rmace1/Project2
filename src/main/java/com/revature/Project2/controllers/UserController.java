package com.revature.Project2.controllers;

import com.revature.Project2.models.JsonResponse;
import com.revature.Project2.models.Post;
import com.revature.Project2.models.User;
import com.revature.Project2.models.UserDTO;
import com.revature.Project2.services.PostService;
import com.revature.Project2.services.UserService;
import com.revature.Project2.util.Email;
import com.revature.Project2.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.File;
import java.util.List;

/**
 * A controller that deals with manipulating users.
 */
@RequestMapping(value = "user")
@RestController
@CrossOrigin(value = "http://3.21.168.108:4200")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * An endpoint that returns all users.
     * @return A list of users.
     */
    @GetMapping
    public List<User> getAllUsers() {
        return this.userService.getAllUsers();
    }

    /**
     * An endpoint to return a single user
     * @param userId The id of the user.
     * @return A UserDTO object for the given user
     */
    @GetMapping("{userId}")
    public UserDTO getOneUser(@PathVariable Integer userId) {
        return new UserDTO(this.userService.getOneUser(userId));
    }

    /**
     * The endpoint to delete a user.
     * @param userId The id of the user.
     * @return A JsonResponse with a message detailing the method's completion
     */
    @DeleteMapping("{userId}")
    public ResponseEntity<JsonResponse> deleteUser(@PathVariable Integer userId) {
        ResponseEntity<JsonResponse> responseEntity;
        Boolean deleted = this.userService.delete(userId);
        if (deleted) {
            responseEntity = ResponseEntity
                    .ok(new JsonResponse("User with id " + userId + " was deleted", null));
        } else {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new JsonResponse("There is not a user with id " + userId, null));
        }

        return responseEntity;
    }

    /**
     * This endpoint will reset the user's password and send an email to the address.
     * @param requestBody The user object inside the Http request body.
     * @return A JsonResponse wth a message and potentially a userDto object.
     */
    @PatchMapping
    public ResponseEntity<JsonResponse> resetPassword(@RequestBody User requestBody) {

        ResponseEntity<JsonResponse> responseEntity;
        //TODO: add password generator potentially
        String newPass = "P4ssw0rd";
        //check if user is valid
        User newUser = requestBody;

        User user = userService.getOneUserByUserName(newUser.getUserName());

        //if not return json response
        if (user == null) {
            responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new JsonResponse("No user found.", null));
            return responseEntity;
        }

        //if so reset password to p4ssw0rd
        user.setPassword(newPass);

        //reset password and send email
        User updatedUser = userService.updateUser(user, null);
        if(updatedUser == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponse("Password unable to be reset.", null));
        }
        Email.sendEmail(user.getEmail(), "Password Reset",
                "Your password has been reset to \"" + newPass + "\"");
        UserDTO userDto = new UserDTO(user);
        //return json response
        responseEntity = ResponseEntity.ok(new JsonResponse("Password reset.", userDto));
        return responseEntity;
    }

    /**
     * The endpoint for registering, or creating, a user with the provided information.
     * @param file The user profile image, optional.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param userName The user's desired username.
     * @param email The user's email address.
     * @param password The user's plaintext email password.
     * @return A JsonResponse that contains a message and the user object.
     */
    @PostMapping
    public ResponseEntity<JsonResponse> registerUser(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam String firstName, @RequestParam String lastName
            , @RequestParam String userName, @RequestParam String email, @RequestParam String password) {

        User newUser = new User(firstName, lastName, userName, email, password);
         if(file != null) {
            newUser.setProfilePic(FileUtil.uploadToS3(newUser, file));
        }else{
             newUser.setProfilePic("https://jwa-p2.s3.us-east-2.amazonaws.com/SocialNetwork/avatar-default-square.jpg");
         }
        User user = this.userService.createUser(newUser);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponse("username already exists in system", null));
        }
        return ResponseEntity.ok(new JsonResponse("user created", user));
    }

    /**
     * The endpoint for updating a user's information
     * @param file The profile picture for the user, optional if not given will be updated.
     * @param firstName The user's first name.
     * @param id The id of the user.
     * @param lastName The user's last name.
     * @param userName The user's username.
     * @param email The user's email address.
     * @param password The user's plaintext password, optional if not given will not be updated.
     * @return A JsonResponse with a message and a user object if it is successful.
     */
    @PutMapping
    public ResponseEntity<JsonResponse> updateUser(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam String firstName,
                           @RequestParam Integer id, @RequestParam String lastName , @RequestParam String userName,
                                                   @RequestParam String email, @RequestParam(required = false) String password) {
        if(password.equals("undefined")){
            password = null;
        }
        User newUser = new User(firstName, lastName, userName, email, password);
        newUser.setId(id);
        Boolean available;

        User oldUser = userService.getOneUser(newUser.getId());
        if(!oldUser.getUserName().equals(newUser.getUserName())){
            available = this.userService.isUserNameAvailable(newUser.getUserName());
            if(!available){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponse("username already in use", null));
            }
        }


        User user = userService.updateUser(newUser, file);

        if(user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponse("error updating user", user));
        }
        return ResponseEntity.ok(new JsonResponse("user updated", user));

    }

    /**
     * This endpoint used to specify when a user likes a post
     * @param userId The id of the user who like the post.
     * @param postId The id of the post liked.
     * @return A response detailing if the post was liked or if it was already liked.
     */
    @PatchMapping("{id}/post/{postId}")
    public ResponseEntity<JsonResponse> likePost(@PathVariable("id") Integer userId, @PathVariable("postId") Integer postId){

        Boolean liked = userService.addLike(userId, postId);

        if(liked) {
            return ResponseEntity.ok(new JsonResponse("Post liked successfully.", null));
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponse("Post already liked.", null));
        }
    }
}