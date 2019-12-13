package com.fmi.pis.kafka;

import com.fmi.pis.kafka.actions.Action;
import com.fmi.pis.noise.diffusion.filters.CannyEdgeFilter;
import com.fmi.pis.noise.diffusion.filters.IsotropicDiffusionFilter;
import com.fmi.pis.noise.diffusion.filters.MirrorFilter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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
    private MirrorFilter mirrorFilter;

    public StreamsStarter(MirrorFilter mirrorFilter) {
        this.mirrorFilter = mirrorFilter;
    }

    public void startStreams() {
        // move ti application properties
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "noise-reducer");
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, "noise-reducer-client");
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
        final StreamsBuilder builder = new StreamsBuilder();
        KStream<String, byte[]> mirrorStream = builder.stream("mirror-topic");
        KStream<String, byte[]> noiseReductionStream = builder.stream("noise-reduction-topic");
        KStream<String, byte[]> edgeStream = builder.stream("edge-topic");
        mirrorStream.foreach(new Action(mirrorFilter, MIRRORED));
        noiseReductionStream.foreach(new Action(new IsotropicDiffusionFilter(), NOISE_REDUCTION));
        edgeStream.foreach(new Action(new CannyEdgeFilter(), EDGE));
        return builder.build();
    }
}
