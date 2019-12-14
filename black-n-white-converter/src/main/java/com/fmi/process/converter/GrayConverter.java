package com.fmi.process.converter;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class GrayConverter implements Converter {

    private static final ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

    @Override
    public BufferedImage convert(final BufferedImage bufferedImage) {
        final BufferedImage bufferedImageGray = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        return op.filter(bufferedImage, bufferedImageGray);
    }
}
