package com.fmi.process.util;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Util {

    /**
     * Makes a deep copy of a {@link BufferedImage}
     *
     * @param bufferedImage the buffered image to be copied
     * @return a copy of the buffered image
     */
    public static BufferedImage deepCopy(final BufferedImage bufferedImage) {
        final ColorModel colorModel = bufferedImage.getColorModel();
        final boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        final WritableRaster raster = bufferedImage.copyData(null);
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }
}
