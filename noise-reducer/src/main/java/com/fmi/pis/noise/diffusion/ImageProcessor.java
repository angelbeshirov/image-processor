package com.fmi.pis.noise.diffusion;

import com.fmi.pis.noise.diffusion.filters.Filter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {
    private Filter filter;
    private BufferedImage image;

    public void setFilter(Filter filter) {
        this.filter = filter;
    }


    /// methods

    /**
     * @param source
     * @param output
     * @brief
     */
    public void process(final File source, final File output) {
        /// load image
        loadImage(source);

        /// pre process image
        preProcess();

        BufferedImage res = filter.filter(image);

        /// post process image
        postProcess();

        /// save image
        saveImage(res, output);
    }

    /**
     * @brief
     */
    protected void preProcess() {
        // TODO IF needed
    }

    /**
     * @brief
     */
    protected void postProcess() {
        // TODO IF needed
    }

    protected void loadImage(final File file) {
        try {
            this.image = ImageIO.read(file);
        } catch (IOException ex) {
            System.out.println("Error while loading the image into memory.");
        }
    }

    protected boolean saveImage(final BufferedImage bufferedImage, final File file) {
        boolean result = true;
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException ex) {
            System.out.println("Error while saving the filtered image.");
            result = false;
        }
        return result;
    }

}