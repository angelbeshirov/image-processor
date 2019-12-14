package com.fmi.process.noise.diffusion;

import com.fmi.process.noise.diffusion.filters.Filter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * not used at the moment
 */
public class ImageProcessor {
    private Filter filter;
    private BufferedImage image;

    public void setFilter(final Filter filter) {
        this.filter = filter;
    }

    /**
     * Processes the image with the filter currently set up.
     *
     * @param source the source filter, containing the image
     * @param output the output file where the image will be saved
     */
    public void process(final File source, final File output) {
        loadImage(source);
        final BufferedImage res = filter.filter(image);
        if (!saveImage(res, output)) {
            System.out.println("Error while processing the image!");
        }
    }

    /**
     * Loads the image from the file into the memory.
     *
     * @param file the file to load
     */
    private void loadImage(final File file) {
        try {
            this.image = ImageIO.read(file);
        } catch (final IOException ex) {
            System.out.println("Error while loading the image into memory.");
        }
    }

    /**
     * Saves the {@link java.io.BufferedReader} into the file.
     *
     * @param bufferedImage the image to be saved
     * @param file          the output file
     * @return {@code true} if the saving of the image was successful and {@code false} otherwise
     */
    private boolean saveImage(final BufferedImage bufferedImage, final File file) {
        boolean result = true;
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (final IOException ex) {
            System.out.println("Error while saving the filtered image.");
            result = false;
        }
        return result;
    }

}