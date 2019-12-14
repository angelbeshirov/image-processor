package com.fmi.process.kafka;

import com.fmi.process.compression.algorithms.SingularValueDecomposition;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

/**
 * @author angel.beshirov
 */
public class StreamsStarter {
    public static final String MIRROR_TOPIC = "compress.topic";
    public static final String COMPRESSED = "compressed";
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
        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<String, byte[]> compressionStream = builder.stream(properties.getProperty(MIRROR_TOPIC));
        compressionStream.foreach(new Action(new SingularValueDecomposition(), COMPRESSED));
        return builder.build();
    }
}
