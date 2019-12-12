package com.fmi.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmi.rest.util.Password;
import com.fmi.rest.util.Util;
import com.fmi.rest.util.Validator;
import com.fmi.rest.model.User;
import com.fmi.rest.services.UserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author angel.beshirov
 */
@Controller
@RequestMapping("/users")
public class UserController {

    private static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    public static final String ID = "id";
    public static final String WINDOWS_FILE_SEPARATOR = "\\";

    // autowired
    private UserService userService;
    private ObjectMapper objectMapper;


    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper,  @Value("${upload.files.dir}") String filesDirectory) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    /**
     * JSON which should be sent
     * {"username":"asd","password":"asd","email":"asdasdasd"}
     */
    // http://localhost:8081/users/register
    @ResponseBody
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody String payload) {
        String error = "";
        System.out.println(payload);
        HttpStatus status = HttpStatus.OK;
        try {
            User user = objectMapper.readValue(payload, User.class);
            if (userService.findByEmail(user.getEmail()) == null) {
                String hashPassword = Password.getSaltedHash(user.getPassword());
                user.setPassword(hashPassword);
                userService.saveUser(user);
            } else {
                status = HttpStatus.BAD_REQUEST;
                error = EMAIL_ALREADY_EXISTS;
            }
        } catch (IOException e) {
            System.out.println("Error while parsing JSON for registration");
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            System.out.println("Error while storing user into the database");
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(error, status);
    }

    @ResponseBody
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody String payload, HttpServletResponse response, HttpSession session) {
        String error = null;
        System.out.println(payload);
        HttpStatus status = HttpStatus.OK;
        try {
            User user = objectMapper.readValue(payload, User.class);
            User databaseUser = userService.findByEmail(user.getEmail());
            if (!Validator.isValid(databaseUser) || !Password.check(user.getPassword(), databaseUser.getPassword())) {
                status = HttpStatus.BAD_REQUEST;
                error = "Wrong email or password";
            } else {
                System.out.println("Successfully logged in");
                session.setAttribute(ID, databaseUser.getId());
                Util.addInsecureCookie(USERNAME, databaseUser.getUsername(), response);
                Util.addSecureCookie(EMAIL, databaseUser.getEmail(), response);
                Util.addSecureCookie(ID, databaseUser.getId().toString(), response);
            }
        } catch (IOException e) {
            System.out.println("Error while parsing JSON for login");
            status = HttpStatus.BAD_REQUEST;
        } catch (Exception e) {
            System.out.println("Error while storing user into the database");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(error, status);
    }

    @GetMapping("/is-logged-in")
    public ResponseEntity<String> isLoggedIn(@CookieValue(value = EMAIL, required = false) String cookie) {
        return new ResponseEntity<>(cookie != null ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Util.deleteSecureHttpOnlyCookie(EMAIL, response);
        Util.deleteSecureHttpOnlyCookie(ID, response);
        Util.deleteInsecureCookie(USERNAME, response);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/all")
    public Iterable<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @ResponseBody
    @GetMapping("/find-by-email")
    public User findByEmail(@RequestParam(name = "email") String email) {
        return userService.findByEmail(email);
    }

}
