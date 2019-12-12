package com.fmi.pis.kafka;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author angel.beshirov
 */
public class CustomKeyValueMapper implements KeyValueMapper<String, byte[], KeyValue<String, byte[]>> {

    @Override
    public KeyValue<String, byte[]> apply(String s, byte[] s2) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(s2));
            File outputfile = new File("src\\main\\resources\\kafka_send.jpg");
            ImageIO.write(bufferedImage, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("key:" + s + " value:" + s2);

        return new KeyValue<>("asd", new byte[2]);
    }
}
