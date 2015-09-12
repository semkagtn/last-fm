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

    @JsonProperty("site")
    private String site;

    @JsonProperty("music")
    private String music;

    @JsonProperty("counters")
    private CountersItem counters;

    public Integer getSex() {
        return sex;
    }

    public String getBdate() {
        return bdate;
    }

    public String getSite() {
        return site;
    }

    public String getMusic() {
        return music;
    }

    public int getId() {
        return id;
    }

    public CountersItem getCounters() {
        return counters;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
