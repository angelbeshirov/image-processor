package com.fmi.process.filters;

import com.fmi.process.util.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Mirrors the image against the vertical central axis.
 */
public class MirrorFilter implements Filter {

    public MirrorFilter() {

    }

    /**
     * Mirrors the image against the vertical central axis.
     *
     * @param image the image to be flipped
     * @return the flipped image
     */
    @Override
    public BufferedImage filter(final BufferedImage image) {
        final BufferedImage result = Util.deepCopy(image);
        int width = result.getWidth() - 1;
        for (int i = 0; i < (result.getWidth()) / 2; i++) {
            for (int j = 0; j < result.getHeight(); j++) {
                final Color color = new Color(result.getRGB(width, j));
                final Color color1 = new Color(result.getRGB(i, j));

                final int r = color.getRed();
                final int g = color.getGreen();
                final int b = color.getBlue();

                final int r1 = color1.getRed();
                final int g1 = color1.getGreen();
                final int b1 = color1.getBlue();

                result.setRGB(i, j, new Color(r, g, b).getRGB());
                result.setRGB(width, j, new Color(r1, g1, b1).getRGB());
            }
            width--;
        }

        return result;
    }
}
