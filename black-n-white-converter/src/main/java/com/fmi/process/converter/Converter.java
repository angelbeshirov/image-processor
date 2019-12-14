package com.fmi.process.converter;

import java.awt.image.BufferedImage;

/**
 * @author angel.beshirov
 */
public interface Converter {
    BufferedImage convert(final BufferedImage bufferedImage);
}
