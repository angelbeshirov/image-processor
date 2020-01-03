package com.fmi.process.kafka;

import com.fmi.process.converters.BlackAndWhiteConverter;
import com.fmi.process.converters.GrayConverter;
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
    public static final String CONVERT_GRAY_TOPIC = "convert.gray.topic";
    public static final String CONVERT_BLACK_AND_WHITE_TOPIC = "convert.black.and.white.topic";
    public static final String OUTPUT_TOPIC = "output.topic";
    private final Properties properties;

    public StreamsStarter(final Properties properties) {
        this.properties = properties;
    }


    public void startStreams() {
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.putAll(properties);
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass().getName());
        streamsConfiguration.put("default.deserialization.exception.handler", LogAndContinueExceptionHandler.class);

        final Topology topology = getTopology();
        final KafkaStreams streams = new KafkaStreams(topology, streamsConfiguration);

        streams.start();
    }

    private Topology getTopology() {
        final org.apache.kafka.streams.StreamsBuilder builder = new org.apache.kafka.streams.StreamsBuilder();
        final KStream<String, byte[]> convertGrayStream = builder.stream(properties.getProperty(CONVERT_GRAY_TOPIC));
        final KStream<String, byte[]> convertBlackNWhiteStream = builder.stream(properties.getProperty(CONVERT_BLACK_AND_WHITE_TOPIC));
        convertGrayStream
                .map(new ConverterKeyValueMapper(new GrayConverter(), GRAY))
                .to(properties.getProperty(OUTPUT_TOPIC));

        convertBlackNWhiteStream
                .map(new ConverterKeyValueMapper(new BlackAndWhiteConverter(), BLACK_N_WHITE))
                .to(properties.getProperty(OUTPUT_TOPIC));

        return builder.build();
    }
}
