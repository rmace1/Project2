package com.revature.Project2.controllers;

import com.revature.Project2.models.JsonResponse;
import com.revature.Project2.models.User;
import com.revature.Project2.models.UserDTO;
import com.revature.Project2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "session")
@CrossOrigin(origins = "http://3.21.168.108:4200", allowCredentials = "true")

/**
 * Contains the methods related to sessions
 */
public class SessionController {
    private UserService userService;

    @Autowired
    public SessionController(UserService userService){
        this.userService = userService;
    }

    /**
     * Logs a user in and creates a session.
     * @param httpSession and requestBody The HttpSession object and http request body.
     * @return The ResponseEntity object with the response body a Json object.
     */
    @PostMapping
    public ResponseEntity<JsonResponse> login(HttpSession httpSession, @RequestBody User requestBody){

        User user = this.userService.validateCredentials(requestBody);

        if(user == null) {
            return ResponseEntity.ok(new JsonResponse("invalid username or password", null));
        }
        UserDTO userDTO = new UserDTO(user);
        httpSession.setAttribute("user-session", userDTO);

        //Tests will fail if the value is an object
        return ResponseEntity.ok(new JsonResponse("login successful", userDTO));

    }

    /**
     * Checks if a session exists.
     * @param httpSession The HttpSession object, which represents the user's session.
     * @return A Json object, which is the response body.
     */
    @GetMapping
    public JsonResponse checkSession(HttpSession httpSession){
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user-session");

        if(userDTO == null)
            return new JsonResponse("no session found", null);


        User user = this.userService.getOneUser(userDTO.getId());

        return new JsonResponse("session found", new UserDTO(user));

    }

    /**
     * Logs a user out and destroys the session.
     * @param httpSession The HttpSession object.
     * @return A Json object.
     */
    @DeleteMapping
    public JsonResponse logout(HttpSession httpSession){
        httpSession.invalidate();
        return new JsonResponse("you have been logged out", null);
    }
}

