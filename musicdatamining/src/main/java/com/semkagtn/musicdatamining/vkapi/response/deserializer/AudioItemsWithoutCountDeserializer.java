package com.semkagtn.musicdatamining.vkapi.response.deserializer;

import com.semkagtn.musicdatamining.utils.JsonUtils;
import com.semkagtn.musicdatamining.vkapi.response.AudioAttachment;
import com.semkagtn.musicdatamining.vkapi.response.AudioItem;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 25.02.16.
 */
public class AudioItemsWithoutCountDeserializer extends JsonDeserializer<List<AudioItem>> {

    @Override
    public List<AudioItem> deserialize(JsonParser jsonParser,
                                       DeserializationContext deserializationContext) throws IOException {
        List<AudioItem> result = new ArrayList<>();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        for (int i = 1; i < node.size(); i++) {
            JsonNode child = node.get(i);
            result.add(JsonUtils.fromJson(child, AudioItem.class));
        }
        return result;
    }
}
