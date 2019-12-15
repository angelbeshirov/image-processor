package com.fmi.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmi.rest.util.Constants;
import com.fmi.rest.util.Password;
import com.fmi.rest.util.Util;
import com.fmi.rest.util.Validator;
import com.fmi.rest.model.User;
import com.fmi.rest.services.UserService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(ImageController.class);
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(final UserService userService, final ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    /**
     * JSON which should be sent
     * {"username":"asd","password":"asd","email":"asdasdasd"}
     */
    @ResponseBody
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody final String payload) {
        String error = "";
        System.out.println(payload);
        HttpStatus status = HttpStatus.OK;
        try {
            final User user = objectMapper.readValue(payload, User.class);
            if (userService.findByEmail(user.getEmail()) == null) {
                final String hashPassword = Password.getSaltedHash(user.getPassword());
                user.setPassword(hashPassword);
                userService.saveUser(user);
            } else {
                status = HttpStatus.BAD_REQUEST;
                error = Constants.EMAIL_ALREADY_EXISTS;
            }
        } catch (final IOException e) {
            LOGGER.error("Error while parsing JSON for registration", e);
            status = HttpStatus.BAD_REQUEST;
        } catch (final Exception e) {
            LOGGER.error("Error while storing user into the database", e);
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(error, status);
    }

    @ResponseBody
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody final String payload, final HttpServletResponse response, final HttpSession session) {
        String error = null;
        HttpStatus status = HttpStatus.OK;
        try {
            final User user = objectMapper.readValue(payload, User.class);
            final User databaseUser = userService.findByEmail(user.getEmail());
            if (!Validator.isValid(databaseUser) || !Password.check(user.getPassword(), databaseUser.getPassword())) {
                status = HttpStatus.BAD_REQUEST;
                error = "Wrong email or password";
            } else {
                LOGGER.info("Successfully logged in");
                session.setAttribute(Constants.COOKIE_ID_NAME, databaseUser.getId());
                Util.addInsecureCookie(Constants.COOKIE_USERNAME_NAME, databaseUser.getUsername(), response);
                Util.addSecureCookie(Constants.COOKIE_EMAIL_NAME, databaseUser.getEmail(), response);
                Util.addSecureCookie(Constants.COOKIE_ID_NAME, databaseUser.getId().toString(), response);
            }
        } catch (final IOException e) {
            LOGGER.error("Error while parsing JSON for login", e);
            status = HttpStatus.BAD_REQUEST;
        } catch (final Exception e) {
            LOGGER.error("Error while storing user into the database", e);
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(error, status);
    }

    @GetMapping("/is-logged-in")
    public ResponseEntity<String> isLoggedIn(@CookieValue(value = Constants.COOKIE_EMAIL_NAME, required = false) final String cookie) {
        return new ResponseEntity<>(cookie != null ? HttpStatus.OK : HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(final HttpServletResponse response, final HttpSession httpSession) {
        httpSession.removeAttribute(Constants.COOKIE_ID_NAME);
        Util.deleteSecureHttpOnlyCookie(Constants.COOKIE_EMAIL_NAME, response);
        Util.deleteSecureHttpOnlyCookie(Constants.COOKIE_ID_NAME, response);
        Util.deleteInsecureCookie(Constants.COOKIE_USERNAME_NAME, response);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/all")
    public Iterable<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @ResponseBody
    @GetMapping("/find-by-email")
    public User findByEmail(@RequestParam(name = "email") final String email) {
        return userService.findByEmail(email);
    }

}
