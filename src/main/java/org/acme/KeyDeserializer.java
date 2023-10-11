package org.acme;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class KeyDeserializer extends ObjectMapperDeserializer<Key> {

    public KeyDeserializer() {
        super(Key.class);
    }
}
