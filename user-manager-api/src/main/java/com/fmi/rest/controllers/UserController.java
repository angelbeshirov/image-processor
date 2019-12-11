package com.fmi.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmi.rest.Validator;
import com.fmi.rest.model.User;
import com.fmi.rest.services.UserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author angel.beshirov
 */
@Controller
@RequestMapping("/users")

public class UserController {

    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    private UserService userService;

    private ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    /**
     * JSON which should be sent
     * {"username":"asd","password":"asd","email":"asdasdasd"}
     */
    // http://localhost:8081/users/register
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody String payload) {
        String error = "";
        System.out.println(payload);
        HttpStatus status = HttpStatus.OK;
        try {
            User user = objectMapper.readValue(payload, User.class);
            if (userService.findByEmail(user.getEmail()) == null) {
                userService.saveUser(user);
            } else {
                status = HttpStatus.BAD_REQUEST;
                error = EMAIL_ALREADY_EXISTS;
            }
        } catch (IOException e) {
            System.out.println("Error while parsing JSON for registration");
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(error, status);
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody String payload, HttpServletResponse response) {
        String error = null;
        System.out.println(payload);
        HttpStatus status = HttpStatus.OK;
        try {
            User user = objectMapper.readValue(payload, User.class);
            User databaseUser = userService.findByEmail(user.getEmail());
            if (!Validator.isValid(databaseUser) || !databaseUser.getPassword().equals(user.getPassword())) {
                status = HttpStatus.BAD_REQUEST;
                error = "Wrong email or password";
            } else {
                System.out.println("Successfully logged in");
                Cookie cookie = new Cookie("username", databaseUser.getUsername());
                cookie.setMaxAge(2 * 60 * 60); // expires in 2 hours

                response.addCookie(cookie);
                Cookie cookie2 = new Cookie("email", databaseUser.getEmail());
                cookie.setMaxAge(2 * 60 * 60); // expires in 2 hours
                cookie.setHttpOnly(true);

                response.addCookie(cookie);
                response.addCookie(cookie2);

                System.out.println("Cookies after login call are ");
            }
        } catch (IOException e) {
            System.out.println("Error while parsing JSON for registration");
            status = HttpStatus.BAD_REQUEST;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie","username="+"testthissiht");
        return ResponseEntity.status(status).headers(headers).build();
    }

    @GetMapping("/is-logged-in")
    public ResponseEntity<String> isLoggedIn(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String emailCookie = null;
        System.out.println("Call to is logged in, email cookie is:" + emailCookie);
        return new ResponseEntity<>(emailCookie != null ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/all")
    @ResponseBody
    public Iterable<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/find-by-email")
    @ResponseBody
    public User findByEmail(@RequestParam(name = "email") String email) {
        return userService.findByEmail(email);
    }

    @GetMapping(value = "/get_image", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] getImage() throws IOException {
        File file = new File("src/main/resources/test_image.jpg");
        InputStream in = new FileInputStream(file);
        return IOUtils.toByteArray(in);
    }

    private void addSecureHttpOnlyCookie(HttpServletResponse response, String email) {

    }

    private void addCookieForUsername(HttpServletResponse response, String username) {
        Cookie cookie = new Cookie("username", username);
        cookie.setMaxAge(2 * 60 * 60); // expires in 2 hours

        response.addCookie(cookie);
    }
}
