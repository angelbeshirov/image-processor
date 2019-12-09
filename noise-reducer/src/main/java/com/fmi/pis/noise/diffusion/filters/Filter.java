package com.fmi.pis.noise.diffusion.filters;

import java.awt.image.BufferedImage;

public interface Filter {
    BufferedImage filter(BufferedImage img);
}

