package com.fmi.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmi.rest.model.Extension;
import com.fmi.rest.model.Image;
import com.fmi.rest.services.ExtensionService;
import com.fmi.rest.services.ImageService;
import com.fmi.rest.util.Constants;
import com.fmi.rest.util.Util;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author angel.beshirov
 */
// TODO this whole controller ideally should be in a separate spring boot application image-manager-api
// TODO but this adds complexity to cookie and session sharing
@Controller
@RequestMapping("/images")
public class ImageController {

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    private final ImageService imageService;
    private final ExtensionService extensionService;
    private final ObjectMapper objectMapper;
    private final String directory;


    // TODO why the HTTPONLY SECURE COOKIE IS NOT AVAILABLE IN THIS CLASS (reason why I am using the session)
    @Autowired
    public ImageController(final ImageService imageService,
                           final ExtensionService extensionService,
                           final ObjectMapper objectMapper,
                           @Value("${upload.files.dir}") final String filesDirectory) {
        this.imageService = imageService;
        this.extensionService = extensionService;
        this.objectMapper = objectMapper;
        this.directory = filesDirectory;
    }

    /**
     * TODO THINK HOW TO FIX
     * Bug: When stopped the session is destroyed, but the cookies remain, so the user is logged in but can not upload files
     */
    @ResponseBody
    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadingPost(final HttpSession session, @RequestParam("filesToUpload[]") final MultipartFile[] uploadingFiles) {
        final HttpStatus status;
        final Integer id = (Integer) session.getAttribute(Constants.COOKIE_ID_NAME);
        if (id != null) {
            final String path = directory + FILE_SEPARATOR + id;
            if (handleDirectories(path)) {
                for (final MultipartFile uploadedFile : uploadingFiles) {
                    final String name = uploadedFile.getOriginalFilename();
                    final String location = path + FILE_SEPARATOR + name;
                    final File file = new File(location);
                    if (!file.exists() && Util.transfer(uploadedFile, file)) {
                        final Extension extension = getExtension(name);
                        imageService.saveImage(new Image(name, location, LocalDate.now(), id, uploadedFile.getSize(), extension));
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
    @Description("Returns all images for the logged in user.")
    public ResponseEntity<String> getAllImages(final HttpSession session) {
        HttpStatus status = HttpStatus.OK;
        String response = null;
        final Integer id = (Integer) session.getAttribute(Constants.COOKIE_ID_NAME);
        if (id != null) {
            try {
                final Iterable<Image> images = imageService.findAllUploadedBy(id);
                response = objectMapper.writeValueAsString(images);
            } catch (IOException ex) {
                // change to log4j in future
                System.out.printf("Error while serializing image data %s", ex.toString());
                status = HttpStatus.BAD_REQUEST;
            }

        } else {
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(response, status);
    }

    @ResponseBody
    @GetMapping(value = "/getAllResults")
    @Description("Returns all results from processed images by the logged in user.")
    public ResponseEntity<String> getAllResults(final HttpSession session) {
        final Integer id = (Integer) session.getAttribute(Constants.COOKIE_ID_NAME);
        String result = null;
        HttpStatus status = HttpStatus.OK;
        if (id != null) {
            final String basePath = imageService.getBasePath(id) + Constants.FILE_SEPARATOR + "results";
            try {
                List<String> allData = getAllDataFromImages(basePath);
                result = objectMapper.writeValueAsString(allData);
            } catch (IOException ex) {
                System.out.println("Error while trying to retrieve image");
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

        } else {
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(result, status);
    }

    @ResponseBody
    @GetMapping(value = "/getImage")
    public ResponseEntity<String> getImage(@RequestParam("file") final String filename, final HttpSession session) {
        final Integer id = (Integer) session.getAttribute(Constants.COOKIE_ID_NAME);
        HttpStatus status = HttpStatus.OK;
        byte[] result = null;
        if (id != null) {
            final String location = imageService.findImageLocation(id, filename);
            final File file = new File(location);
            try {
                final InputStream in = new FileInputStream(file);
                result = IOUtils.toByteArray(in);
                in.close();
            } catch (IOException ex) {
                System.out.println("Error while trying to retrieve image");
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

        } else {
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(Base64.getEncoder().encodeToString(result), status);
    }

    @ResponseBody
    @GetMapping(value = "/download", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> download(@RequestParam("file") final String filename, final HttpSession session) {
        final Integer id = (Integer) session.getAttribute(Constants.COOKIE_ID_NAME);
        byte[] result = null;
        if (id != null) {
            final String location = imageService.findImageLocation(id, filename);
            final File file = new File(location);
            try (final InputStream in = new FileInputStream(file)) {
                result = IOUtils.toByteArray(in);
            } catch (IOException ex) {
                System.out.println("Error while trying to retrieve image");
            }
        }

        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Disposition", "attachment");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(result);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteImage(@RequestParam("file") final String filename, final HttpSession session) {
        final Integer id = (Integer) session.getAttribute(Constants.COOKIE_ID_NAME);
        HttpStatus status = HttpStatus.OK;
        if (id != null) {
            final Integer fileId = imageService.findImageId(id, filename);
            final String location = imageService.findImageLocation(id, filename);
            imageService.deleteById(fileId);

            final File file = new File(location);
            if (file.delete()) {
                System.out.printf("File %s deleted successfully", filename);
            } else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return new ResponseEntity<>(status);
    }

    private boolean handleDirectories(final String path) {
        final File directory = new File(path);
        return directory.exists() || directory.mkdirs();
    }

    private Extension getExtension(final String name) {
        if (name == null || !name.contains(Constants.DOT)) {
            return null;
        }

        final String extension = name.substring(name.indexOf(Constants.DOT));
        return extensionService.findByValue(extension);
    }

    // each String is the base64 encoded binary content of an image
    private List<String> getAllDataFromImages(String path) throws IOException {
        List<String> result = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            result = walk.filter(Files::isRegularFile)
                    .map(x -> {
                        byte[] binaryData = null;
                        final File file = x.toFile();
                        try (final InputStream in = new FileInputStream(file)) {
                            binaryData = IOUtils.toByteArray(in);
                        } catch (IOException ex) {
                            System.out.println("Error while getting binary data from files.");
                        }

                        return Base64.getEncoder().encodeToString(binaryData);
                    }).collect(Collectors.toList());
        }

        return result;
    }
}
