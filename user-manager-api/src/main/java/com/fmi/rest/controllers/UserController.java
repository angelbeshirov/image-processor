package com.fmi.rest.controllers;

import com.fmi.rest.UserRepository;
import com.fmi.rest.model.User;
import com.fmi.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author angel.beshirov
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // http://localhost:8081/users/add?username="blabla"&email=sthelse&password=123
    @PostMapping(path = "/add")
    public @ResponseBody
    String addNewUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        User user = new User(username, password, email);
        userService.saveUser(user);
        return "Saved";
    }

    @GetMapping("/all")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/find-by-email")
    public @ResponseBody
    User findByEmail(@RequestParam(name = "email") String email) {
        return userService.findByEmail(email);
    }
}
