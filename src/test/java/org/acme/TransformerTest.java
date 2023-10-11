package org.acme;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
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

    @Test
    void dup() {
        companion.produce(String.class, String.class).fromRecords(List.of(new ProducerRecord<>("in", "hi", "hello")));

        ConsumerTask<String, String> task = companion.consume(String.class).fromTopics("out", 1);

        task.awaitCompletion();

        assertThat(task.count()).isEqualTo(1);
        assertThat(task.getFirstRecord().value()).isEqualTo("hello|hello");
    }

    @Test
    void nil() {
        companion.produce(String.class, String.class).fromRecords(List.of(new ProducerRecord<>("out", "hi", "hello")));

        ConsumerTask<String, String> task = companion.consume(String.class).fromTopics("end", 1);

        task.awaitCompletion();

        assertThat(task.count()).isEqualTo(1);
        assertThat(task.getFirstRecord().value()).isEqualTo("nil");
    }

    @Test
    void startToEnd() {
        companion.produce(String.class, String.class).fromRecords(List.of(new ProducerRecord<>("in", "hi", "hello")));

        ConsumerTask<String, String> task = companion.consume(String.class).fromTopics("end", 1);

        task.awaitCompletion();

        assertThat(task.count()).isEqualTo(1);
        assertThat(task.getFirstRecord().value()).isEqualTo("nil");
    }
}
