package com.fmi.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmi.rest.model.Task;
import com.fmi.rest.services.ImageService;
import com.fmi.rest.util.Constants;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

/**
 * @author angel.beshirov
 */
@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final ImageService imageService;
    private final ObjectMapper objectMapper;
    private final Producer<String, byte[]> producer;
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    @Autowired
    public TaskController(final ImageService imageService,
                          final ObjectMapper objectMapper,
                          final Producer<String, byte[]> producer,
                          @Value("${upload.files.dir}") final String filesDirectory) {
        this.imageService = imageService;
        this.objectMapper = objectMapper;
        this.producer = producer;
    }

    @ResponseBody
    @PostMapping(value = "/perform")
    public ResponseEntity<String> perform(@RequestBody final String payload, final HttpSession session) {
        HttpStatus status = HttpStatus.OK;
        final Integer id = (Integer) session.getAttribute(Constants.COOKIE_ID_NAME);
        if (id != null) {
            try {
                final Task task = objectMapper.readValue(payload, Task.class);
                final String location = imageService.findImageLocation(id, task.getFileName());
                final String outputLocation = location.substring(0, location.indexOf(task.getFileName())) + "results" + FILE_SEPARATOR + task.getFileName();
                final File file = new File(location);
                try (final InputStream in = new FileInputStream(file)) {
                    byte[] data = IOUtils.toByteArray(in);
                    if (sendImage(outputLocation, task, data) == null) {
                        status = HttpStatus.INTERNAL_SERVER_ERROR;
                    }
                } catch (IOException ex) {
                    System.out.println("Error while trying to retrieve image.");
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
                } catch (InterruptedException | ExecutionException e) {
                    System.out.printf("Error with kafka %s", e.toString());
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
                }
            } catch (IOException e) {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(status);
    }

    private RecordMetadata sendImage(final String outputLocation, final Task task, final byte[] data) throws ExecutionException, InterruptedException {
        final String topic = determineTopic(task);
        if (topic != null) {
            // TODO location should be changed to also contain the type of task the specific component is supposed to do
            // TODO custom serializer and deserialzier again for kafka
            final ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, outputLocation, data);
            return producer.send(record).get();
        } else {
            return null;
        }
    }

    private String determineTopic(Task task) {
        String resultTopic = null;
        // all of these should come from application.properties file
        switch (task.getAction()) {
            case COMPRESSION:
                resultTopic = "compression-topic";
                break;
            case NOISE_REDUCTION:
                resultTopic = "noise-reduction-topic";
                break;
            case MIRROR:
                resultTopic = "mirror-topic";
                break;
            case CONVERT_TO_GRAY:
            case CONVERT_TO_BLACK_AND_WHITE:
                resultTopic = "convert-topic";
                break;
            case EXTRACT_EDGES:
                resultTopic = "edge-topic";
            case NOT_SUPPORTED:
        }

        return resultTopic;
    }


}
