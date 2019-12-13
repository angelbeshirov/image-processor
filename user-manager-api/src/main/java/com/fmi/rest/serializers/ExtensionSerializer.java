package com.fmi.rest.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fmi.rest.model.Extension;

import java.io.IOException;

/**
 * @author angel.beshirov
 */
public class ExtensionSerializer extends StdSerializer<Extension> {

    public static final char JSON_STRING = '\"';

    public ExtensionSerializer() {
        this(null);
    }

    public ExtensionSerializer(Class<Extension> t) {
        super(t);
    }

    @Override
    public void serialize(final Extension value, final JsonGenerator jgen, final SerializerProvider provider)
            throws IOException {
        jgen.writeRawValue(JSON_STRING + value.getName() + JSON_STRING);
    }

}
