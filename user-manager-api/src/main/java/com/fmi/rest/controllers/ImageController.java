package com.fmi.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmi.rest.model.User;
import com.fmi.rest.services.UserService;
import com.fmi.rest.util.Password;
import com.fmi.rest.util.Util;
import com.fmi.rest.util.Validator;
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
import java.net.http.HttpResponse;

/**
 * @author angel.beshirov
 */
@Controller
@RequestMapping("/images")
public class ImageController {

    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String WINDOWS_FILE_SEPARATOR = "\\";

    // autowired
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final String directory;


    @Autowired
    public ImageController(UserService userService, ObjectMapper objectMapper, @Value("${upload.files.dir}") String filesDirectory) {
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.directory = filesDirectory;
    }

    /**
     * JSON which should be sent
     * {"username":"asd","password":"asd","email":"asdasdasd"}
     */
//    // http://localhost:8081/users/register
//    @PostMapping(path = "/upload", consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public ResponseEntity<String> register(@RequestBody String payload) {
//        String error = "";
//        System.out.println(payload);
//        HttpStatus status = HttpStatus.OK;
//        try {
//            User user = objectMapper.readValue(payload, User.class);
//            if (userService.findByEmail(user.getEmail()) == null) {
//                String hashPassword = Password.getSaltedHash(user.getPassword());
//                user.setPassword(hashPassword);
//                userService.saveUser(user);
//            } else {
//                status = HttpStatus.BAD_REQUEST;
//                error = EMAIL_ALREADY_EXISTS;
//            }
//        } catch (IOException e) {
//            System.out.println("Error while parsing JSON for registration");
//            status = HttpStatus.BAD_REQUEST;
//        } catch (Exception e) {
//            System.out.println("Error while storing user into the database");
//            status = HttpStatus.BAD_REQUEST;
//        }
//        return new ResponseEntity<>(error, status);
//    }


    //

    /**
     * TODO THINK HOW TO FIX
     * Bug: When stopped the session is destroyed, but the cookies remain, so the user is logged in but can not upload filex
     */
    @ResponseBody
    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadingPost(HttpServletRequest request, HttpSession session,
                                                @RequestParam("filesToUpload[]") MultipartFile[] uploadingFiles) throws IOException {
        HttpStatus status;
        Integer id = (Integer) session.getAttribute(UserController.ID);
        if (id != null) {
            String path = directory + WINDOWS_FILE_SEPARATOR + id;
            if (handleDirectories(path)) {
                for (MultipartFile uploadedFile : uploadingFiles) {
                    // TODO WINDOWS_FILE_SEPARATOR should be set using some variable from the system
                    File file = new File(path + WINDOWS_FILE_SEPARATOR + uploadedFile.getOriginalFilename());
                    uploadedFile.transferTo(file);
                }
                status = HttpStatus.OK;
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(status);
    }

    private boolean handleDirectories(String path) {
        File directory = new File(path);
        return directory.exists() || directory.mkdirs();
    }
}
