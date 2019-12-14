package com.fmi.pis.kafka;

import com.fmi.pis.converter.BlackAndWhiteConverter;
import com.fmi.pis.converter.GrayConverter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

/**
 * @author angel.beshirov
 */
public class StreamsStarter {

    public static final String GRAY = "gray";
    public static final String BLACK_N_WHITE = "black-n-white";

    public void startStreams() {
        // move to application properties
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "black-n-white-converter");
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, "black-n-white-converter-client");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass().getName());
        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");
        streamsConfiguration.put("default.deserialization.exception.handler", LogAndContinueExceptionHandler.class);
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);

        final Topology topology = getTopology();
        final KafkaStreams streams = new KafkaStreams(topology, streamsConfiguration);

        streams.start();
    }

    private Topology getTopology() {
        final org.apache.kafka.streams.StreamsBuilder builder = new org.apache.kafka.streams.StreamsBuilder();
        KStream<String, byte[]> convertGrayStream = builder.stream("convert-gray-topic");
        KStream<String, byte[]> convertBlackNWhiteStream = builder.stream("convert-black-n-white-topic");
        convertGrayStream.foreach(new Action(new GrayConverter(), GRAY));
        convertBlackNWhiteStream.foreach(new Action(new BlackAndWhiteConverter(), BLACK_N_WHITE));
        return builder.build();
    }
}
