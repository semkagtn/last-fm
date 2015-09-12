package com.semkagtn.lastfm.vkapi.response.deserializer;

import com.semkagtn.lastfm.utils.JsonUtils;
import com.semkagtn.lastfm.vkapi.response.AudioAttachment;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 11.09.15.
 */
public class OnlyAudioAttachmentsDeserializer extends JsonDeserializer<List<AudioAttachment>> {

    @Override
    public List<AudioAttachment> deserialize(JsonParser jsonParser,
                                             DeserializationContext deserializationContext) throws IOException {
        List<AudioAttachment> result = new ArrayList<>();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        for (int i = 0; i < node.size(); i++) {
            JsonNode child = node.get(i);
            JsonNode typeNode = child.get("type");
            if (typeNode != null && typeNode.getTextValue().equals("audio")) {
                result.add(JsonUtils.fromJson(child, AudioAttachment.class));
            }
        }
        return result;
    }
}
