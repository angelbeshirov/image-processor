package com.fmi.process.compression.algorithms;

import java.awt.image.BufferedImage;

public interface Algorithm {
    BufferedImage compress(final BufferedImage source);
}
