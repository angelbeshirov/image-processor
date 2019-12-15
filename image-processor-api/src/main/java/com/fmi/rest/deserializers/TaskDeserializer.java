package com.fmi.rest.deserializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fmi.rest.model.Action;
import com.fmi.rest.model.Extension;
import com.fmi.rest.model.Task;

import java.io.IOException;

/**
 * @author angel.beshirov
 */
public class TaskDeserializer extends StdDeserializer<Task> {


    public TaskDeserializer() {
        this(null);
    }

    @Override
    public Task deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        JsonNode actionNode = node.get("action");
        JsonNode fileNode = node.get("file");

        int action = actionNode.intValue();
        String fileName = fileNode.asText();

        return new Task(getAction(action), fileName);
    }

    public TaskDeserializer(Class<Extension> t) {
        super(t);
    }

    private Action getAction(int action) {
        Action result = Action.NOT_SUPPORTED;
        switch (action) {
            case 0:
                result = Action.COMPRESSION;
                break;
            case 1:
                result = Action.NOISE_REDUCTION;
                break;
            case 2:
                result = Action.MIRROR;
                break;
            case 3:
                result = Action.CONVERT_TO_GRAY;
                break;
            case 4:
                result = Action.CONVERT_TO_BLACK_AND_WHITE;
                break;
            case 5:
                result = Action.EXTRACT_EDGES;
                break;
        }

        return result;
    }

}
