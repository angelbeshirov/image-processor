package com.fmi.pis.noise.util;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Util {

    public static BufferedImage deepCopy(BufferedImage bufferedImage) {
        ColorModel cm = bufferedImage.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bufferedImage.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
