package com.fmi.process.kafka;

import com.fmi.process.converters.Converter;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author angel.beshirov
 */
public class ConverterKeyValueMapper implements KeyValueMapper<String, byte[], KeyValue<String, byte[]>> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterKeyValueMapper.class);
    public static final String RESULTS_DIRECTORY = "results";
    private static final String JPG = ".jpg";
    private static final String JPEG = ".jpeg";
    private static final String PNG = ".png";
    private static final char DOT = '.';
    public static final char UNDERSCORE = '_';

    private final Converter converter;
    private final String keyWord;

    public ConverterKeyValueMapper(final Converter converter, final String keyWord) {
        this.converter = converter;
        this.keyWord = UNDERSCORE + keyWord + UNDERSCORE;
    }

    @Override
    public KeyValue<String, byte[]> apply(final String fileLocation, final byte[] data) {
        KeyValue<String, byte[]> result = null;
        LOGGER.info("Received message for " + fileLocation);

        try (final InputStream is = new ByteArrayInputStream(data);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            final BufferedImage bufferedImage = ImageIO.read(is);
            final BufferedImage compressedImage = converter.convert(bufferedImage);

            final String format = fileLocation.substring(fileLocation.indexOf(DOT) + 1);
            final String outputLocation = fileLocation.substring(0, fileLocation.lastIndexOf(FILE_SEPARATOR)) + FILE_SEPARATOR +
                    RESULTS_DIRECTORY + getFileResultName(fileLocation, format);

            ImageIO.write(compressedImage, format, baos);
            baos.flush();

            result = new KeyValue<>(outputLocation, baos.toByteArray());
        } catch (final IOException e) {
            LOGGER.error("Error while writing the result from the operation", e);
        }

        return result;
    }

    private String getFileResultName(final String location, final String format) {
        final String fileName = location.substring(location.lastIndexOf(FILE_SEPARATOR), getExtensionIndex(location));
        return fileName + keyWord + formatter.format(LocalDateTime.now()) + DOT + format;
    }

    private int getExtensionIndex(final String directory) {
        final int res1 = directory.indexOf(JPG);
        final int res2 = directory.indexOf(JPEG);
        final int res3 = directory.indexOf(PNG);

        if (res1 != -1) {
            return res1;
        }

        if (res2 != -1) {
            return res2;
        }

        return res3;
    }
}
