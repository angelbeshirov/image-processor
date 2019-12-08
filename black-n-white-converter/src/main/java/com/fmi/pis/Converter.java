package com.fmi.pis;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

public class Converter {

    private static final ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

    public BufferedImage convertToGray(final BufferedImage source) {
        final BufferedImage bufferedImageGray = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        return op.filter(source, bufferedImageGray);
    }

    public BufferedImage convertToBlackNWhite(final BufferedImage source) {
        final BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        final Graphics2D graphic = result.createGraphics();
        graphic.drawImage(source, 0, 0, Color.WHITE, null);
        graphic.dispose();

        return result;
    }
}
