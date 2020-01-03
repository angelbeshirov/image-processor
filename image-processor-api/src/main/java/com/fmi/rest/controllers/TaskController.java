package com.fmi.rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmi.rest.model.Task;
import com.fmi.rest.services.ImageService;
import com.fmi.rest.util.Constants;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.core.env.Environment;
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
    private final static Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    private final ImageService imageService;
    private final ObjectMapper objectMapper;
    private final Environment environment;
    private final Producer<String, byte[]> producer;

    @Autowired
    public TaskController(final ImageService imageService,
                          final ObjectMapper objectMapper,
                          final Producer<String, byte[]> producer,
                          final Environment environment,
                          @Value("${upload.files.dir}") final String filesDirectory) {
        this.imageService = imageService;
        this.objectMapper = objectMapper;
        this.producer = producer;
        this.environment = environment;
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
                final File file = new File(location);
                try (final InputStream in = new FileInputStream(file)) {
                    byte[] data = IOUtils.toByteArray(in);
                    if (sendImage(location, task, data) == null) {
                        status = HttpStatus.INTERNAL_SERVER_ERROR;
                    }
                } catch (IOException e) {
                    LOGGER.error("Error while trying to retrieve image.", e);
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
                } catch (InterruptedException | ExecutionException e) {
                    LOGGER.error("Kafka error.", e);
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
                }
            } catch (IOException e) {
                LOGGER.error("Error while trying to deserialize JSON payload.", e);
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            status = HttpStatus.UNAUTHORIZED;
        }

        return new ResponseEntity<>(status);
    }

    private RecordMetadata sendImage(final String location, final Task task, final byte[] data) throws ExecutionException, InterruptedException {
        final String topic = determineTopic(task);
        if (topic != null) {
            final ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, location, data);
            return producer.send(record).get();
        } else {
            return null;
        }
    }

    private String determineTopic(final Task task) {
        String resultTopic = null;
        switch (task.getAction()) {
            case COMPRESSION:
                resultTopic = environment.getProperty("compression.topic");
                break;
            case NOISE_REDUCTION:
                resultTopic = environment.getProperty("noise.reduction.topic");
                break;
            case MIRROR:
                resultTopic = environment.getProperty("mirror.topic");
                break;
            case CONVERT_TO_GRAY:
                resultTopic = environment.getProperty("convert.to.gray.topic");
                break;
            case CONVERT_TO_BLACK_AND_WHITE:
                resultTopic = environment.getProperty("convert.to.black.and.white.topic");
                break;
            case EXTRACT_EDGES:
                resultTopic = environment.getProperty("edge.topic");
            case NOT_SUPPORTED:
        }

        return resultTopic;
    }


}
