package com.semkagtn.lastfm.vkapi.response;

import com.semkagtn.lastfm.utils.JsonUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 30.08.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserItem {

    @JsonProperty("id")
    private int id;

    @JsonProperty("sex")
    private Integer sex;

    @JsonProperty("bdate")
    private String bdate;

    @JsonProperty("counters")
    private CountersItem counters;

    @JsonProperty("photo_50")
    private String photo50;

    @JsonProperty("last_seen")
    private LastSeenItem lastSeen;

    public Integer getSex() {
        return sex;
    }

    public String getBdate() {
        return bdate;
    }

    public int getId() {
        return id;
    }

    public CountersItem getCounters() {
        return counters;
    }

    public String getPhoto50() {
        return photo50;
    }

    public LastSeenItem getLastSeen() {
        return lastSeen;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
