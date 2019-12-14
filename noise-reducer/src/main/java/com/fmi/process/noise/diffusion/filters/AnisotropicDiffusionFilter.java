package com.fmi.process.noise.diffusion.filters;

import com.fmi.process.noise.util.Util;

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
    public AnisotropicDiffusionFilter(final double lambda, final double kappa, final int iterations) {
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
    public BufferedImage filter(final BufferedImage image) {
        final BufferedImage result = Util.deepCopy(image);
        for (int k = 0; k < this.iterations; k++) {
            for (int i = 1; i < result.getWidth() - 1; i++) {
                for (int j = 1; j < result.getHeight() - 1; j++) {
                    final int rgbNorth = result.getRGB(i, j - 1);
                    final int rgbSouth = result.getRGB(i, j + 1);
                    final int rgbWest = result.getRGB(i - 1, j);
                    final int rgbEast = result.getRGB(i + 1, j);
                    final int rgbCenter = result.getRGB(i, j);

                    /// red diffs
                    final int redNewNorth = (((rgbNorth >> 16) & 0xFF) -
                            ((rgbCenter >> 16) & 0xFF));

                    final int redNewWest = (((rgbWest >> 16) & 0xFF) -
                            ((rgbCenter >> 16) & 0xFF));

                    final int redNewSouth = (((rgbSouth >> 16) & 0xFF) -
                            ((rgbCenter >> 16) & 0xFF));

                    final int redNewEast = (((rgbEast >> 16) & 0xFF) -
                            ((rgbCenter >> 16) & 0xFF));


                    /// green diffs
                    final int greenNewNorth = (((rgbNorth >> 8) & 0xFF) -
                            ((rgbCenter >> 8) & 0xFF));

                    final int greenNewWest = (((rgbWest >> 8) & 0xFF) -
                            ((rgbCenter >> 8) & 0xFF));

                    final int greenNewSouth = (((rgbSouth >> 8) & 0xFF) -
                            ((rgbCenter >> 8) & 0xFF));

                    final int greenNewEast = (((rgbEast >> 8) & 0xFF) -
                            ((rgbCenter >> 8) & 0xFF));

                    /// blue diffs
                    final int blueNewNorth = ((rgbNorth & 0xFF) -
                            (rgbCenter & 0xFF));

                    final int blueNewWest = ((rgbWest & 0xFF) -
                            (rgbCenter & 0xFF));

                    final int blueNewSouth = ((rgbSouth & 0xFF) -
                            (rgbCenter & 0xFF));

                    final int blueNewEast = ((rgbEast & 0xFF) -
                            (rgbCenter & 0xFF));

                    /// scale with fluxDerivative red channel
                    final double redNorthScaled = fluxDerivative(redNewNorth);
                    final double redSouthScaled = fluxDerivative(redNewSouth);
                    final double redWestScaled = fluxDerivative(redNewWest);
                    final double redEastScaled = fluxDerivative(redNewEast);

                    /// scale with fluxDerivative green channel
                    final double greenNorthScaled = fluxDerivative(greenNewNorth);
                    final double greenSouthScaled = fluxDerivative(greenNewSouth);
                    final double greenWestScaled = fluxDerivative(greenNewWest);
                    final double greenEastScaled = fluxDerivative(greenNewEast);

                    /// scale with fluxDerivative blue channel
                    final double blueNorthScaled = fluxDerivative(blueNewNorth);
                    final double blueSouthScaled = fluxDerivative(blueNewSouth);
                    final double blueWestScaled = fluxDerivative(blueNewWest);
                    final double blueEastScaled = fluxDerivative(blueNewEast);

                    final int redCenter = ((rgbCenter >> 16) & 0xFF);
                    final int redNew = (int) (redCenter + lambda * (redNorthScaled * redNewNorth + redSouthScaled * redNewSouth
                            + redEastScaled * redNewEast + redWestScaled * redNewWest));

                    final int greenCenter = ((rgbCenter >> 8) & 0xFF);
                    final int greenNew = (int) (greenCenter + lambda * (greenNorthScaled * greenNewNorth + greenSouthScaled * greenNewSouth
                            + greenEastScaled * greenNewEast + greenWestScaled * greenNewWest));

                    final int blueCenter = ((rgbCenter & 0xFF));
                    final int blueNew = (int) (blueCenter + lambda * (blueNorthScaled * blueNewNorth + blueSouthScaled * blueNewSouth
                            + blueEastScaled * blueNewEast + blueWestScaled * blueNewWest));

                    final Color color = new Color(redNew, greenNew, blueNew);
                    result.setRGB(i, j, color.getRGB());
                }
            }
        }

        return result;
    }

    private double fluxDerivative(final double intensity) {
        return Math.exp(-Math.pow(intensity / this.kappa, 2));
    }

}