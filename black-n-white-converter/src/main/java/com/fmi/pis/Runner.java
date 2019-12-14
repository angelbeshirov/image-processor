package com.fmi.pis;

import com.fmi.pis.kafka.StreamsStarter;

public class Runner {

    public static void main(final String[] args) {
//        try {
//            final BufferedImage source = ImageIO.read(new File("src/main/resources/switzerland.jpg"));
//            final GrayConverter grayConverter = new GrayConverter();
//            ImageIO.write(grayConverter.convertToGray(source), "jpg", new File("src/main/resources/gray_switzerland1.jpg"));
//            ImageIO.write(grayConverter.convertToBlackNWhite(source), "jpg", new File("src/main/resources/black-n-white_switzerland1.jpg"));
//        } catch (final IOException ex) {
//            System.out.println("Error while performing I/O operations with the images.");
//        }

        StreamsStarter streamsStarter = new StreamsStarter();
        streamsStarter.startStreams();

    }
}
