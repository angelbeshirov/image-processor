package com.fmi.process.compression;

import java.awt.image.BufferedImage;

public interface Algorithm {
    BufferedImage compress(final BufferedImage source);
}
