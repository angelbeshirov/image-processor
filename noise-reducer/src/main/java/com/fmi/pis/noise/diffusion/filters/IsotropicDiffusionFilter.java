package com.fmi.pis.noise.diffusion.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class IsotropicDiffusionFilter implements Filter {
    private final int iterations;
    private final double lambda;

    /**
     * @brief default setup for isotropic diffusion filter
     */
    public IsotropicDiffusionFilter() {
        this.iterations = 20;
        this.lambda = 0.25;
    }

    /**
     * @param iterations number of iterations
     * @param lambda     width of one timestep
     * @brief adjust setup for isotropic diffusion filter
     */
    public IsotropicDiffusionFilter(int iterations, double lambda) {
        this.iterations = iterations;
        this.lambda = lambda;
    }

    /**
     * @param img
     * @brief
     */
    @Override
    public BufferedImage filter(BufferedImage img) {
        for (int k = 0; k < this.iterations; k++) {
            for (int i = 1; i < img.getWidth() - 1; i++) {
                for (int j = 1; j < img.getHeight() - 1; j++) {
                    int rgbNorth = img.getRGB(i, j - 1);
                    int rgbSouth = img.getRGB(i, j + 1);
                    int rgbWest = img.getRGB(i - 1, j);
                    int rgbEast = img.getRGB(i + 1, j);
                    int rgbCenter = img.getRGB(i, j);

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
                    img.setRGB(i, j, color.getRGB());
                }
            }
        }

        return img;
    }
}

