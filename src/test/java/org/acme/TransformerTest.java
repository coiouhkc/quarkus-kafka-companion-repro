package org.acme;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void clearTopics() {
        companion.topics().clear("in", "out");
    }

    @Test
    void dup1() {
        companion.produce(String.class, String.class).fromRecords(List.of(new ProducerRecord<>("in", "hi", "hello")));

        ConsumerTask<String, String> task = companion.consume(String.class).fromTopics("out", 1);

        task.awaitCompletion();

        assertThat(task.count()).isEqualTo(1);
        assertThat(task.getFirstRecord().value()).isEqualTo("hello|hello");
    }

    @Test
    void dup2() {
        ConsumerTask<String, String> task = companion.consume(String.class).fromTopics("out", 1);

        task.awaitNoRecords(Duration.ofSeconds(10));

        assertThat(task.count()).isEqualTo(0);

        // alternatively just test anything not involving kafka/ specified topic
        // assertThat("hello").isEqualTo("hello");
    }
}
