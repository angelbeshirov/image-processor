package com.fmi.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Properties;

/**
 * @author angel.beshirov
 */
public class KafkaProducerTest {

    private final static String TOPIC = "test";
    private final static String BOOTSTRAP_SERVERS =
            "localhost:9092";

//        private static Producer<String, byte[]> createProducer() {
//            Properties props = new Properties();
//            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                    BOOTSTRAP_SERVERS);
//            props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
//            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//                    StringSerializer.class.getName());
//            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//                    StringSerializer.class.getName());
//            return new KafkaProducer<>(props);
//        }

    private static Producer<String, byte[]> createProducerImages() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "ImageProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                ByteArraySerializer.class.getName());
        return new KafkaProducer<>(props);
    }

//        private static void runProducer(final int sendMessageCount) throws Exception {
//            final Producer<String, String> producer = createProducer();
//            long time = System.currentTimeMillis();
//
//            try {
//                for (long index = 0; index < sendMessageCount; index++) {
//                    final ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, String.valueOf(index), "Hello Mom " + index);
//
//                    RecordMetadata metadata = producer.send(record).get();
//
//                    long elapsedTime = System.currentTimeMillis() - time;
//                    System.out.printf("sent record(key=%s value=%s) " + "meta(partition=%d, offset=%d) time=%d\n",
//                            record.key(), record.value(), metadata.partition(),
//                            metadata.offset(), elapsedTime);
//                }
//            } finally {
//                producer.flush();
//                producer.close();
//            }
//        }

    private static void runProducer(final int sendMessageCount) throws Exception {
        final Producer<String, byte[]> producer = createProducerImages();
        long time = System.currentTimeMillis();
        File file = new File("src\\main\\resources\\black-n-white_switzerland.jpg");
        BufferedImage bufferedImage = ImageIO.read(file);

        try {
            //for (long index = 0; index < sendMessageCount; index++) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write( bufferedImage, "jpg", baos );
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                baos.close();
                final ProducerRecord<String, byte[]> record = new ProducerRecord<>(TOPIC, String.valueOf(12), imageInByte);

                RecordMetadata metadata = producer.send(record).get();

                long elapsedTime = System.currentTimeMillis() - time;
                //System.out.printf("sent record(key=%s value=%s) " + "meta(partition=%d, offset=%d) time=%d\n",
                   //     record.key(), record.value(), metadata.partition(),
                     //   metadata.offset(), elapsedTime);
            //}
        } finally {
            producer.flush();
            producer.close();
        }
    }

        public static void main(String[] args) throws Exception {
            runProducer(10);
        }
    }
