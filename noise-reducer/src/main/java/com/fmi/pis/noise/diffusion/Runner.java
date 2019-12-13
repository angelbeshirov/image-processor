package com.fmi.pis.noise.diffusion;

import com.fmi.pis.kafka.StreamsStarter;
import com.fmi.pis.noise.diffusion.filters.AnisotropicDiffusionFilter;
import com.fmi.pis.noise.diffusion.filters.CannyEdgeFilter;
import com.fmi.pis.noise.diffusion.filters.IsotropicDiffusionFilter;
import com.fmi.pis.noise.diffusion.filters.MirrorFilter;
import com.sun.jdi.event.StepEvent;

import java.io.File;

public class Runner {
    public static void main(String... args) {
//        try {
//
//            ImageProcessor p3 = new ImageProcessor();
//            p3.setFilter(new AnisotropicDiffusionFilter(0.1, 10, 50));
//            p3.process(new File("src/main/resources/noisy.png"), new File("src/main/resources/noisy_output_anisotropic_1.png"));
//
//            ImageProcessor p4 = new ImageProcessor();
//            p4.setFilter(new AnisotropicDiffusionFilter(0.1, 10, 50));
//            p4.process(new File("src/main/resources/noisy.png"), new File("src/main/resources/noisy_output_anisotropic_variant_1.png"));
//
//            ImageProcessor p5 = new ImageProcessor();
//            p5.setFilter(new IsotropicDiffusionFilter());
//            p5.process(new File("src/main/resources/noisy.png"), new File("src/main/resources/noisy_output_isotropic_2.png"));
//
//            ImageProcessor p6 = new ImageProcessor();
//            p6.setFilter(new MirrorFilter());
//            p6.process(new File("src/main/resources/triangle.jpg"), new File("src/main/resources/mirrored_triangle.jpg"));
//
//            ImageProcessor p7 = new ImageProcessor();
//            p7.setFilter(new CannyEdgeFilter());
//            p7.process(new File("src/main/resources/noisy.png"), new File("src/main/resources/noisy_canny_edge.png"));
//
//        } catch (Exception ex) {
//            System.out.println("Error while reducing the noise of the images");
//        }
        StreamsStarter streamsStarter = new StreamsStarter(new MirrorFilter());
        streamsStarter.startStreams();
    }
}
