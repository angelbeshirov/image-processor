package com.fmi.pis.noise.diffusion.filters;

import com.fmi.pis.noise.util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Reduces noise from image based on the isotropic diffusion.
 */
public class AnisotropicDiffusionFilter implements Filter {
    private final double lambda;
    private final double kappa;
    private final int iterations;

    /**
     * Default setup for anisotropic diffusion filter
     */
    public AnisotropicDiffusionFilter() {
        this.lambda = 0.1d;
        this.kappa = 5d;
        this.iterations = 50;
    }

    /**
     * Setup for anisotropic diffusion filter
     */
    public AnisotropicDiffusionFilter(double lambda, double kappa, int iterations) {
        this.lambda = lambda;
        this.kappa = kappa;
        this.iterations = iterations;
    }

    /**
     * Performs the noise reduction based on the anisotropic diffusion.
     *
     * @param image the image to be processed
     * @return the processed image
     */
    @Override
    public BufferedImage filter(BufferedImage image) {
        final BufferedImage result = Util.deepCopy(image);
        for (int k = 0; k < this.iterations; k++) {
            for (int i = 1; i < result.getWidth() - 1; i++) {
                for (int j = 1; j < result.getHeight() - 1; j++) {
                    int rgbNorth = result.getRGB(i, j - 1);
                    int rgbSouth = result.getRGB(i, j + 1);
                    int rgbWest = result.getRGB(i - 1, j);
                    int rgbEast = result.getRGB(i + 1, j);
                    int rgbCenter = result.getRGB(i, j);

                    /// red diffs
                    int redNewNorth = (((rgbNorth >> 16) & 0xFF) -
                            ((rgbCenter >> 16) & 0xFF));

                    int redNewWest = (((rgbWest >> 16) & 0xFF) -
                            ((rgbCenter >> 16) & 0xFF));

                    int redNewSouth = (((rgbSouth >> 16) & 0xFF) -
                            ((rgbCenter >> 16) & 0xFF));

                    int redNewEast = (((rgbEast >> 16) & 0xFF) -
                            ((rgbCenter >> 16) & 0xFF));


                    /// green diffs
                    int greenNewNorth = (((rgbNorth >> 8) & 0xFF) -
                            ((rgbCenter >> 8) & 0xFF));

                    int greenNewWest = (((rgbWest >> 8) & 0xFF) -
                            ((rgbCenter >> 8) & 0xFF));

                    int greenNewSouth = (((rgbSouth >> 8) & 0xFF) -
                            ((rgbCenter >> 8) & 0xFF));

                    int greenNewEast = (((rgbEast >> 8) & 0xFF) -
                            ((rgbCenter >> 8) & 0xFF));

                    /// blue diffs
                    int blueNewNorth = ((rgbNorth & 0xFF) -
                            (rgbCenter & 0xFF));

                    int blueNewWest = ((rgbWest & 0xFF) -
                            (rgbCenter & 0xFF));

                    int blueNewSouth = ((rgbSouth & 0xFF) -
                            (rgbCenter & 0xFF));

                    int blueNewEast = ((rgbEast & 0xFF) -
                            (rgbCenter & 0xFF));

                    /// scale with fluxDerivative red channel
                    double redNorthScaled = fluxDerivative(redNewNorth);
                    double redSouthScaled = fluxDerivative(redNewSouth);
                    double redWestScaled = fluxDerivative(redNewWest);
                    double redEastScaled = fluxDerivative(redNewEast);

                    /// scale with fluxDerivative green channel
                    double greenNorthScaled = fluxDerivative(greenNewNorth);
                    double greenSouthScaled = fluxDerivative(greenNewSouth);
                    double greenWestScaled = fluxDerivative(greenNewWest);
                    double greenEastScaled = fluxDerivative(greenNewEast);

                    /// scale with fluxDerivative blue channel
                    double blueNorthScaled = fluxDerivative(blueNewNorth);
                    double blueSouthScaled = fluxDerivative(blueNewSouth);
                    double blueWestScaled = fluxDerivative(blueNewWest);
                    double blueEastScaled = fluxDerivative(blueNewEast);

                    int redCenter = ((rgbCenter >> 16) & 0xFF);
                    int redNew = (int) (redCenter + lambda * (redNorthScaled * redNewNorth + redSouthScaled * redNewSouth
                            + redEastScaled * redNewEast + redWestScaled * redNewWest));

                    int greenCenter = ((rgbCenter >> 8) & 0xFF);
                    int greenNew = (int) (greenCenter + lambda * (greenNorthScaled * greenNewNorth + greenSouthScaled * greenNewSouth
                            + greenEastScaled * greenNewEast + greenWestScaled * greenNewWest));

                    int blueCenter = ((rgbCenter & 0xFF));
                    int blueNew = (int) (blueCenter + lambda * (blueNorthScaled * blueNewNorth + blueSouthScaled * blueNewSouth
                            + blueEastScaled * blueNewEast + blueWestScaled * blueNewWest));

                    Color color = new Color(redNew, greenNew, blueNew);
                    result.setRGB(i, j, color.getRGB());
                }
            }
        }

        return result;
    }

    private double fluxDerivative(double intensity) {
        return Math.exp(-Math.pow(intensity / this.kappa, 2));
    }

}