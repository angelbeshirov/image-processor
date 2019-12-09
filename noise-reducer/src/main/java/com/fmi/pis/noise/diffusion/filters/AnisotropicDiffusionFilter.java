package com.fmi.pis.noise.diffusion.filters;

/// imports

import com.fmi.pis.noise.util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AnisotropicDiffusionFilter implements Filter {
    /// members
    private final double lambda;
    private final double kappa;
    private final int iterations;

    public AnisotropicDiffusionFilter() {
        this.lambda = 0.1d;
        this.kappa = 5d;
        this.iterations = 50;
    }

    public AnisotropicDiffusionFilter(double lambda, double kappa, int iterations) {
        this.lambda = lambda;
        this.kappa = kappa;
        this.iterations = iterations;
    }

    @Override
    public BufferedImage filter(BufferedImage image) {
        BufferedImage result = Util.deepCopy(image);
        for (int k = 0; k < this.iterations; k++) {
            for (int i = 1; i < image.getWidth() - 1; i++) {
                for (int j = 1; j < image.getHeight() - 1; j++) {
                    int rgb_north = image.getRGB(i, j - 1);
                    int rgb_south = image.getRGB(i, j + 1);
                    int rgb_west = image.getRGB(i - 1, j);
                    int rgb_east = image.getRGB(i + 1, j);
                    int rgb_center = image.getRGB(i, j);

                    /// red diffs
                    int red_new_N = (((rgb_north >> 16) & 0xFF) -
                            ((rgb_center >> 16) & 0xFF));

                    int red_new_W = (((rgb_west >> 16) & 0xFF) -
                            ((rgb_center >> 16) & 0xFF));

                    int red_new_S = (((rgb_south >> 16) & 0xFF) -
                            ((rgb_center >> 16) & 0xFF));

                    int red_new_E = (((rgb_east >> 16) & 0xFF) -
                            ((rgb_center >> 16) & 0xFF));


                    /// green diffs
                    int green_new_N = (((rgb_north >> 8) & 0xFF) -
                            ((rgb_center >> 8) & 0xFF));

                    int green_new_W = (((rgb_west >> 8) & 0xFF) -
                            ((rgb_center >> 8) & 0xFF));

                    int green_new_S = (((rgb_south >> 8) & 0xFF) -
                            ((rgb_center >> 8) & 0xFF));

                    int green_new_E = (((rgb_east >> 8) & 0xFF) -
                            ((rgb_center >> 8) & 0xFF));

                    /// blue diffs
                    int blue_new_N = ((rgb_north & 0xFF) -
                            (rgb_center & 0xFF));

                    int blue_new_W = ((rgb_west & 0xFF) -
                            (rgb_center & 0xFF));

                    int blue_new_S = ((rgb_south & 0xFF) -
                            (rgb_center & 0xFF));

                    int blue_new_E = ((rgb_east & 0xFF) -
                            (rgb_center & 0xFF));

                    /// scale with flux_derivative red channel
                    double red_cN = flux_derivative(red_new_N);
                    double red_cS = flux_derivative(red_new_S);
                    double red_cW = flux_derivative(red_new_W);
                    double red_cE = flux_derivative(red_new_E);

                    /// scale with flux_derivative green channel
                    double green_cN = flux_derivative(green_new_N);
                    double green_cS = flux_derivative(green_new_S);
                    double green_cW = flux_derivative(green_new_W);
                    double green_cE = flux_derivative(green_new_E);

                    /// scale with flux_derivative blue channel
                    double blue_cN = flux_derivative(blue_new_N);
                    double blue_cS = flux_derivative(blue_new_S);
                    double blue_cW = flux_derivative(blue_new_W);
                    double blue_cE = flux_derivative(blue_new_E);

                    int red_center = ((rgb_center >> 16) & 0xFF);
                    int red_new = (int) (red_center + lambda * (red_cN * red_new_N + red_cS * red_new_S
                            + red_cE * red_new_E + red_cW * red_new_W));

                    int green_center = ((rgb_center >> 8) & 0xFF);
                    int green_new = (int) (green_center + lambda * (green_cN * green_new_N + green_cS * green_new_S
                            + green_cE * green_new_E + green_cW * green_new_W));

                    int blue_center = ((rgb_center & 0xFF));
                    int blue_new = (int) (blue_center + lambda * (blue_cN * blue_new_N + blue_cS * blue_new_S
                            + blue_cE * blue_new_E + blue_cW * blue_new_W));

                    /// set new "diffused" pixel values: red, green and blue
                    Color color = new Color(red_new, green_new, blue_new);
                    result.setRGB(i, j, color.getRGB());
                    //result.setRGB(i, j, color.getRGB());
                }
            }
            //image = image2;
        }

        return result;
    }

    protected double flux_derivative(double intensity) {
        return Math.exp(-Math.pow(intensity / this.kappa, 2));
    }

}