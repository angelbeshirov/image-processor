package com.fmi.rest.services;


import com.fmi.rest.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("users")
public class UserResource {

    @Autowired
    private UserRepository employeeRepository;

    @ResponseBody
    @PostMapping("/register")
    public String register() {
        return "register";
    }

    @ResponseBody
    @PostMapping("/login")
    public String login(@RequestParam(name="username") String username, @RequestParam(name="username") String password) {
        return "login";
    }


}
