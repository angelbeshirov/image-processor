package com.fmi.process.kafka;

import com.fmi.process.converter.Converter;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author angel.beshirov
 */
public class Action implements ForeachAction<String, byte[]> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final Logger LOGGER = LoggerFactory.getLogger(Action.class);
    private static final String JPG = ".jpg";
    private static final String JPEG = ".jpeg";
    private static final String PNG = ".png";
    private static final char DOT = '.';

    private final Converter converter;
    private final String keyWord;

    public Action(final Converter converter, final String keyWord) {
        this.converter = converter;
        this.keyWord = '_' + keyWord + '_';
    }

    @Override
    public void apply(final String outputLocation, final byte[] data) {
        // this output location will probably be removed and all results will be sent to another kafka topic
        // from which another application will consume and save the result files
        // or it will be passed as key to the next topic
        LOGGER.info("Received message for " + outputLocation);
        try (final InputStream is = new ByteArrayInputStream(data)) {
            if (handleDirectories(outputLocation.substring(0, outputLocation.lastIndexOf(FILE_SEPARATOR)))) {
                final BufferedImage bufferedImage = ImageIO.read(is);
                final BufferedImage result = converter.convert(bufferedImage);

                final String format = outputLocation.substring(outputLocation.indexOf(DOT) + 1);
                final File outputFile = new File(outputLocation.substring(0, getExtensionIndex(outputLocation)) + keyWord + formatter.format(LocalDateTime.now()) + DOT + format);
                ImageIO.write(result, format, outputFile);
            }
        } catch (final IOException e) {
            System.out.printf("Error while writing the result from the operation! %s", e.toString());
        }
    }

    private boolean handleDirectories(final String path) {
        final File directory = new File(path);
        return directory.exists() || directory.mkdirs();
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
