package com.fmi.pis.noise;

import java.awt.image.ColorConvertOp;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CannyFilter {
    public static void main(String args[]) throws IOException {
        BufferedImage photo = ImageIO.read(new File("src/main/resources/noisy.png"));
        File output = new File("src/main/resources/output.png");
        //create the detector
        CannyFilterUtil detector = new CannyFilterUtil();

        //adjust its parameters as desired
        detector.setLowThreshold(0.5f);
        detector.setHighThreshold(1f);

        //apply it to an image
        detector.setSourceImage(photo);
        detector.process();
        BufferedImage edges = detector.getEdgesImage();
        //ImageIO.write(edges,"JPG",output);
        BufferedImage rgbImage = new BufferedImage(edges.getWidth(),
                edges.getHeight(), BufferedImage.TYPE_INT_RGB);

        ColorConvertOp op = new ColorConvertOp(null);
        op.filter(edges, rgbImage);
        ImageIO.write(rgbImage, "JPEG", output);
    }
}
