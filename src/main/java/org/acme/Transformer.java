package org.acme;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Transformer {
    @Incoming("in")
    @Outgoing("out")
    public Record<Key, Value> dup(Record<Key, Value> record) {
        return Record.of (record.key(), new Value(record.value().getValue() + "|" + record.value().getValue()));
    }
}
