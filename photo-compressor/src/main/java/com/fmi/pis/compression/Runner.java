package com.fmi.pis.compression;

import com.fmi.pis.compression.algorithms.SingularValueDecomposition;
import com.fmi.pis.compression.util.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Runner {

    // used for demonstration of the results and the loss of data in different percentages
    public static void main(final String[] args) throws IOException {
        final BufferedImage bufferedImage = ImageIO.read(new File("src\\main\\resources\\mgdth.jpg"));
        final SingularValueDecomposition svd = new SingularValueDecomposition(bufferedImage);
        svd.compress(0.6);
        Util.writeImage(svd.getOutput(), "src\\main\\resources\\mgdth_50%.jpg", "jpg");

        final SingularValueDecomposition svd2 = new SingularValueDecomposition(bufferedImage);
        svd2.compress(0.9);
        Util.writeImage(svd2.getOutput(), "src\\main\\resources\\mgdth_90%.jpg", "jpg");
    }
}
