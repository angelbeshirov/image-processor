package com.fmi.pis.noise.diffusion.filters;

import com.fmi.pis.noise.util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Reduces noise from image based on the isotropic diffusion.
 */
public class IsotropicDiffusionFilter implements Filter {
    private final int iterations;
    private final double lambda;

    /**
     * Default setup for isotropic diffusion filter
     */
    public IsotropicDiffusionFilter() {
        this.iterations = 20;
        this.lambda = 0.25;
    }

    /**
     * Setup for isotropic diffusion filter.
     *
     * @param iterations number of iterations
     * @param lambda     width of one time step
     */
    public IsotropicDiffusionFilter(int iterations, double lambda) {
        this.iterations = iterations;
        this.lambda = lambda;
    }

    /**
     * Performs the noise reduction based on the isotropic diffusion.
     *
     * @param image the image to be processed
     * @return the processed image
     */
    @Override
    public BufferedImage filter(BufferedImage image) {
        BufferedImage buff = Util.deepCopy(image);
        for (int k = 0; k < this.iterations; k++) {
            for (int i = 1; i < buff.getWidth() - 1; i++) {
                for (int j = 1; j < buff.getHeight() - 1; j++) {
                    int rgbNorth = buff.getRGB(i, j - 1);
                    int rgbSouth = buff.getRGB(i, j + 1);
                    int rgbWest = buff.getRGB(i - 1, j);
                    int rgbEast = buff.getRGB(i + 1, j);
                    int rgbCenter = buff.getRGB(i, j);

                    int red = (int) (((rgbCenter >> 16) & 0xFF) +
                            this.lambda * (-4 * ((rgbCenter >> 16) & 0xFF) +
                                    ((rgbWest >> 16) & 0xFF) +
                                    ((rgbEast >> 16) & 0xFF) +
                                    ((rgbSouth >> 16) & 0xFF) +
                                    ((rgbNorth >> 16) & 0xFF)));

                    int green = (int) (((rgbCenter >> 8) & 0xFF) +
                            this.lambda * (-4 * ((rgbCenter >> 8) & 0xFF) +
                                    ((rgbWest >> 8) & 0xFF) +
                                    ((rgbEast >> 8) & 0xFF) +
                                    ((rgbSouth >> 8) & 0xFF) +
                                    ((rgbNorth >> 8) & 0xFF)));

                    int blue = (int) (((rgbCenter & 0xFF) +
                            this.lambda * (-4 * (rgbCenter & 0xFF) +
                                    (rgbWest & 0xFF) +
                                    (rgbEast & 0xFF) +
                                    (rgbSouth & 0xFF) +
                                    (rgbNorth & 0xFF))));

                    Color color = new Color(red, green, blue);
                    buff.setRGB(i, j, color.getRGB());
                }
            }
        }

        return buff;
    }
}

