package org.acme;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.smallrye.reactive.messaging.kafka.companion.ConsumerTask;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;

@QuarkusTestResource(KafkaCompanionResource.class)
@QuarkusTest
public class TransformerTest {
    @InjectKafkaCompanion
    KafkaCompanion companion;

    @BeforeEach
    void setUp() {
        companion.registerSerde(Key.class, new KeySerializer(), new KeyDeserializer());
        companion.registerSerde(Value.class, new ValueSerializer(), new ValueDeserializer());
    }

    @Test
    void dup() {
        companion.produce(Key.class, Value.class).fromRecords(List.of(new ProducerRecord<>("in", new Key("hi"), new Value("hello"))));

        ConsumerTask<Key, Value> task = companion.consume(Key.class, Value.class).fromTopics("out", 1);

        task.awaitCompletion();

        assertThat(task.count()).isEqualTo(1);
    }
}
