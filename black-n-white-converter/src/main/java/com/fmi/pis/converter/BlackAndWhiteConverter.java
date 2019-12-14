package com.fmi.pis.converter;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author angel.beshirov
 */
public class BlackAndWhiteConverter implements Converter {

    @Override
    public BufferedImage convert(BufferedImage bufferedImage) {
        final BufferedImage result = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        final Graphics2D graphic = result.createGraphics();
        graphic.drawImage(bufferedImage, 0, 0, Color.WHITE, null);
        graphic.dispose();

        return result;
    }
}
