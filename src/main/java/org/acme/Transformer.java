package org.acme;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Transformer {
    @Incoming("in1")
    @Outgoing("out1")
    public Record<String, String> dup(Record<String, String> record) {
        return Record.of (record.key(), record.value() + "|" + record.value());
    }

    @Incoming("in2")
    @Outgoing("out2")
    public Record<String, String> nil(Record<String, String> record) {
        return Record.of (record.key(), "nil");
    }
}
