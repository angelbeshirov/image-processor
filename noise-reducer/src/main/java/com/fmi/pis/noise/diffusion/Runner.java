package com.fmi.pis.noise.diffusion;

/// imports

import com.fmi.pis.noise.diffusion.filters.AnisotropicDiffusionFilter;
import com.fmi.pis.noise.diffusion.filters.AnisotropicDiffusionFilterVariant;
import com.fmi.pis.noise.diffusion.filters.IsotropicDiffusionFilter;

import java.io.File;

public class Runner {
    /**
     * @param args
     * @brief
     */
    public static void main(String... args) {
        try {

            ImageProcessor p3 = new ImageProcessor();
            p3.setFilter(new AnisotropicDiffusionFilter(0.1, 20, 50));
            File file = new File("src/main/resources/noisy_test_2.png");
            p3.process(file, new File("src/main/resources/noisy_output_1_1.png"));

            // diffusion
            p3.setFilter(new IsotropicDiffusionFilter());
            File file_diff = new File("src/main/resources/noisy_diffusion.png");
            p3.process(file, file_diff);
            //new ImageProcessor().process(new File("src/main/resources/noisy.png"), new File("src/main/resources/noisy_output_2.png"));


            ImageProcessor p4 = new ImageProcessor();
            p4.setFilter(new AnisotropicDiffusionFilterVariant(20));
            p4.process(new File("src/main/resources/noisy.png"), new File("src/main/resources/noisy_output_4.png"));


        } catch (Exception ex) {

        }
    }
}
