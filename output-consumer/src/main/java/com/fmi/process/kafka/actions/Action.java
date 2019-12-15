package com.fmi.process.kafka.actions;

import org.apache.kafka.streams.kstream.ForeachAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author angel.beshirov
 */
public class Action implements ForeachAction<String, byte[]> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Action.class);
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    @Override
    public void apply(final String outputLocation, final byte[] payload) {
        LOGGER.info("Received file for saving into {}", outputLocation);
        if (handleDirectories(outputLocation.substring(0, outputLocation.lastIndexOf(FILE_SEPARATOR)))) {
            try (final FileOutputStream fos = new FileOutputStream(outputLocation)) {
                fos.write(payload);
            } catch (final IOException e) {
                LOGGER.error("Error while saving output file to {}", outputLocation, e);
            }
        }
    }

    private boolean handleDirectories(final String path) {
        final File directory = new File(path);
        return directory.exists() || directory.mkdirs();
    }
}
