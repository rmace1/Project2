package com.revature.Project2.controllers;

import com.revature.Project2.models.JsonResponse;
import com.revature.Project2.models.User;
import com.revature.Project2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "session")
@CrossOrigin(origins = "http://localhost:9000", allowCredentials = "true")
public class SessionController {
    private UserService userService;

    @Autowired
    public SessionController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public JsonResponse login(HttpSession httpSession, @RequestBody User requestBody){

        User user = this.userService.validateCredentials(requestBody);

        if(user == null) {
            return new JsonResponse("invalid username or password", null);
        }

        httpSession.setAttribute("user-session", user.getId());

        return new JsonResponse("login successful", user);

    }

    @GetMapping
    public JsonResponse checkSession(HttpSession httpSession){
        Integer userId = (Integer) httpSession.getAttribute("user-session");

        if(userId == null)
            return new JsonResponse("no session found", null);


        User user = this.userService.getOneUser(userId);

        return new JsonResponse("session found", user);

    }

    @DeleteMapping
    public JsonResponse logout(HttpSession httpSession){
        httpSession.invalidate();
        return new JsonResponse("you have been logged out", null);
    }
}

