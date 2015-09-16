package com.semkagtn.musicdatamining.vkapi.response;

import com.semkagtn.musicdatamining.utils.JsonUtils;
import com.semkagtn.musicdatamining.vkapi.response.deserializer.OnlyAudioAttachmentsDeserializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

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

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
