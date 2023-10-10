package org.acme;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Transformer {
    @Incoming("in")
    @Outgoing("out")
    public Record<String, String> dup(Record<String, String> record) {
        return Record.of (record.key(), record.value() + "|" + record.value());
    }
}
