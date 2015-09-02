package com.semkagtn.lastfm.vkapi.response;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 31.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WallItem {

    @JsonProperty("date")
    private long date;

    @JsonProperty("attachments")
    @JsonDeserialize(using = OnlyAudioAttachmentsDeserializer.class)
    private List<AudioAttachment> attachments;

    public long getDate() {
        return date;
    }

    public List<AudioAttachment> getAttachments() {
        return attachments;
    }

    public static class OnlyAudioAttachmentsDeserializer extends JsonDeserializer<List<AudioAttachment>> {

        @Override
        public List<AudioAttachment> deserialize(JsonParser jsonParser,
                                                 DeserializationContext deserializationContext) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            List<AudioAttachment> result = new ArrayList<>();
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            for (int i = 0; i < node.size(); i++) {
                JsonNode child = node.get(i);
                JsonNode typeNode = child.get("type");
                if (typeNode != null && typeNode.getTextValue().equals("audio")) {
                    result.add(objectMapper.readValue(child, AudioAttachment.class));
                }
            }
            return result;
        }
    }
}
