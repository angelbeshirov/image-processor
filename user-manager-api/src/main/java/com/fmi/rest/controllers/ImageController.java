package com.fmi.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmi.rest.model.Image;
import com.fmi.rest.services.UserService;
import com.fmi.rest.services.ImageService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author angel.beshirov
 */
// TODO this whole controller ideally should be in a separate spring boot application image-manager-api
    // TODO but this adds complexity to cookie and session sharing
@Controller
@RequestMapping("/images")
public class ImageController {

    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String WINDOWS_FILE_SEPARATOR = "\\";

    // autowired
    private final UserService userService;
    private final ImageService imageService;
    private final ObjectMapper objectMapper;
    private final String directory;


    // TODO why the HTTPONLY SECURE COOKIE IS NOT AVAILABLE IN THIS CLASS (reason why I am using the session)
    @Autowired
    public ImageController(ImageService imageService,
                           UserService userService,
                           ObjectMapper objectMapper,
                           @Value("${upload.files.dir}") final String filesDirectory) {
        this.userService = userService;
        this.imageService = imageService;
        this.objectMapper = objectMapper;
        this.directory = filesDirectory;
    }

    /**
     * TODO THINK HOW TO FIX
     * Bug: When stopped the session is destroyed, but the cookies remain, so the user is logged in but can not upload files
     */
    @ResponseBody
    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadingPost(HttpSession session, @RequestParam("filesToUpload[]") MultipartFile[] uploadingFiles) throws IOException {
        HttpStatus status;
        Integer id = (Integer) session.getAttribute(UserController.ID);
        if (id != null) {
            String path = directory + WINDOWS_FILE_SEPARATOR + id;
            if (handleDirectories(path)) {
                for (MultipartFile uploadedFile : uploadingFiles) {
                    // TODO WINDOWS_FILE_SEPARATOR should be set using some variable from the system
                    final String name = uploadedFile.getOriginalFilename();
                    final String location = path + WINDOWS_FILE_SEPARATOR + name;
                    File file = new File(location);
                    if (!file.exists()) {
                        uploadedFile.transferTo(file);
                        // this might be good to be fixed as well i.e. size should be long not int change in the DB and test if works
                        imageService.saveImage(new Image(name, location, LocalDate.now(), id, (int) uploadedFile.getSize(), getExtension(name)));
                    }
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

    @ResponseBody
    @GetMapping(value = "/getAll")
    // gets all images for user TODO put it in annotation description @RestDescription or something
    public ResponseEntity<String> uploadingPost(HttpSession session) throws IOException {
        HttpStatus status = HttpStatus.OK;
        String response = null;
        Integer id = (Integer) session.getAttribute(UserController.ID);
        if (id != null) {
            Iterable<Image> images = imageService.findAllUploadedBy(id);
            response = objectMapper.writeValueAsString(images);
        } else {
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(response, status);
    }

    @ResponseBody
    @GetMapping(value = "/download", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public byte[] getImage(@RequestParam("file") final String filename, HttpSession session) throws IOException {
        Integer id = (Integer) session.getAttribute(UserController.ID);
        byte[] result = null;
        if(id != null) {
            String location = imageService.findImageLocation(id, filename);
            File file = new File(location);
            InputStream in = new FileInputStream(file);
            result = IOUtils.toByteArray(in);
        }


        return result;
    }

    private boolean handleDirectories(String path) {
        File directory = new File(path);
        return directory.exists() || directory.mkdirs();
    }

    // TODO this should be retrieved from the database based on the extension of the file, if the extension is wrote return BAD_REQUEST 400 STATUS CODE
    private Integer getExtension(String name) {
        if (name == null) {
            return -1; // boom boom, shouldnt happen
        }
        if (name.endsWith(".png")) {
            return 1;
        } else if (name.endsWith(".jpg")) {
            return 2;
        } else if (name.endsWith(".jpeg")) {
            return 3;
        }

        return -1;
    }
}
