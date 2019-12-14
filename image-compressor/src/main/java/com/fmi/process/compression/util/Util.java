package com.fmi.process.compression.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class Util {

    /**
     * Makes a deep copy of a {@link BufferedImage}
     *
     * @param bufferedImage the buffered image to be copied
     * @return a copy of the buffered image
     */
    public static BufferedImage deepCopy(final BufferedImage bufferedImage) {
        final ColorModel cm = bufferedImage.getColorModel();
        final boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        final WritableRaster raster = bufferedImage.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * Saves the image to the specified file.
     *
     * @param bufferedImage the buffered image to be saved
     * @param filename      the path to the file where the image should be saved
     * @param extension     the extension of the saved image
     * @throws IOException if there was an error while writing the image
     */
    public static void writeImage(final BufferedImage bufferedImage, final String filename, final String extension) throws IOException {
        final File output = new File(filename);
        ImageIO.write(bufferedImage, extension, output);
    }
}
