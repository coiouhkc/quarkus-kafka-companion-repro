package org.acme;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class ValueDeserializer extends ObjectMapperDeserializer<Value> {

    public ValueDeserializer() {
        super(Value.class);
    }
}
