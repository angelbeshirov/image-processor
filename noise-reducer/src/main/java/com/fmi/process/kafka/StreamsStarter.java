package com.fmi.process.kafka;

import com.fmi.process.kafka.actions.Action;
import com.fmi.process.noise.diffusion.filters.CannyEdgeFilter;
import com.fmi.process.noise.diffusion.filters.IsotropicDiffusionFilter;
import com.fmi.process.noise.diffusion.filters.MirrorFilter;
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

    public static final String MIRRORED = "mirrored";
    public static final String NOISE_REDUCTION = "noise_reduction";
    public static final String EDGE = "edge";
    public static final String MIRROR_TOPIC = "mirror.topic";
    public static final String NOISE_REDUCTION_TOPIC = "noise.reduction.topic";
    public static final String EDGE_TOPIC = "edge.topic";
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
        final KStream<String, byte[]> mirrorStream = builder.stream(properties.getProperty(MIRROR_TOPIC));
        final KStream<String, byte[]> noiseReductionStream = builder.stream(properties.getProperty(NOISE_REDUCTION_TOPIC));
        final KStream<String, byte[]> edgeStream = builder.stream(properties.getProperty(EDGE_TOPIC));
        mirrorStream.foreach(new Action(new MirrorFilter(), MIRRORED));
        noiseReductionStream.foreach(new Action(new IsotropicDiffusionFilter(), NOISE_REDUCTION));
        edgeStream.foreach(new Action(new CannyEdgeFilter(), EDGE));
        return builder.build();
    }
}
