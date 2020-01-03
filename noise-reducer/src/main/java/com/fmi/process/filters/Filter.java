package com.fmi.process.filters;

import java.awt.image.BufferedImage;

/**
 * Functional interface for performing different filter operations on an image.
 */
public interface Filter {
    BufferedImage filter(BufferedImage img);
}

