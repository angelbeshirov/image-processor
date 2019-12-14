package com.fmi.pis.kafka;

import com.fmi.pis.converter.Converter;
import org.apache.kafka.streams.kstream.ForeachAction;

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

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private static final char DOT = '.';
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
    private static final String JPG = ".jpg";
    private static final String JPEG = ".jpeg";
    private static final String PNG = ".png";

    private final Converter converter;
    private final String keyWord;

    public Action(final Converter converter, final String keyWord) {
        this.converter = converter;
        this.keyWord = '_' + keyWord + '_';
    }

    @Override
    public void apply(String outputLocation, byte[] data) {
        // this output location will probably be removed and all results will be sent to another kafka topic
        // from which another application will consume and save the result files
        // or it will be passed as key to the next topic
        try (InputStream is = new ByteArrayInputStream(data)) {
            if (handleDirectories(outputLocation.substring(0, outputLocation.lastIndexOf(FILE_SEPARATOR)))) {
                BufferedImage bufferedImage = ImageIO.read(is);
                BufferedImage result = converter.convert(bufferedImage);

                String format = outputLocation.substring(outputLocation.indexOf(DOT) + 1);
                File outputFile = new File(outputLocation.substring(0, toWhere(outputLocation)) + keyWord + formatter.format(LocalDateTime.now()) + DOT + format);
                ImageIO.write(result, format, outputFile);
            }
        } catch (IOException e) {
            System.out.printf("Error while writing the result from the operation! %s", e.toString());
        }
    }

    private boolean handleDirectories(final String path) {
        final File directory = new File(path);
        return directory.exists() || directory.mkdirs();
    }


    // TODO extension has to be deserialized from the key or think of better way
    private int toWhere(String directory) {
        int res1 = directory.indexOf(JPG);
        int res2 = directory.indexOf(JPEG);
        int res3 = directory.indexOf(PNG);

        if (res1 != -1) {
            return res1;
        }

        if (res2 != -1) {
            return res2;
        }

        return res3;
    }
}
