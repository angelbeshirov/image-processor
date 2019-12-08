package com.fmi.pis;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Runner {

    public static void main(final String[] args) {
        try {
            final BufferedImage source = ImageIO.read(new File("src/main/resources/switzerland.jpg"));
            final Converter converter = new Converter();
            ImageIO.write(converter.convertToGray(source), "jpg", new File("src/main/resources/gray_switzerland1.jpg"));
            ImageIO.write(converter.convertToBlackNWhite(source), "jpg", new File("src/main/resources/black-n-white_switzerland1.jpg"));
        } catch (final IOException ex) {
            System.out.println("Error while performing I/O operations with the images.");
        }

    }
}
