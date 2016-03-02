package com.semkagtn.musicdatamining.vkapi.response;

import com.semkagtn.musicdatamining.utils.JsonUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by semkagtn on 24.02.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExecuteGetGroupMembersResponse extends BaseVkResponse {

    @JsonProperty("response")
    private List<Long> response;

    public List<Long> getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
