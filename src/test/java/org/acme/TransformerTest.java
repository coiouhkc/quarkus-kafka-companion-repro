package org.acme;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.kafka.Record;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

@QuarkusTest
public class TransformerTest {
    @Inject
    @Any
    InMemoryConnector connector;

    @Test
    void dup() {
        InMemorySource<Record<String, String>> source = connector.source("in");
        InMemorySink<Record<String, String>> sink = connector.sink("out");

        source.send(Record.of("hi", "hello"));

        assertThat(sink.received()).hasSize(1);
        assertThat(sink.received().get(0).getPayload().key()).isEqualTo("hi");
        assertThat(sink.received().get(0).getPayload().value()).isEqualTo("hello|hello");
    }
}
