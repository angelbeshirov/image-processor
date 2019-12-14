package com.fmi.pis.converter;

import java.awt.image.BufferedImage;

/**
 * @author angel.beshirov
 */
public interface Converter {
    BufferedImage convert(final BufferedImage bufferedImage);
}
