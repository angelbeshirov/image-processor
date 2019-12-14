package com.fmi.process;

import com.fmi.process.kafka.StreamsStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class Runner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    public static void main(final String[] args) {
        try (final InputStream input = new FileInputStream("src\\main\\resources\\application.properties")) {
            final Properties properties = new Properties();
            properties.load(input);
            final StreamsStarter streamsStarter = new StreamsStarter(properties);
            streamsStarter.startStreams();
        } catch (final FileNotFoundException ex) {
            LOGGER.error("The properties file for the application was not found", ex);
        } catch (final IOException ex) {
            LOGGER.error("IO Error with the properties file", ex);
        }
    }
}
