package com.semkagtn.musicdatamining.vkapi.response;

import com.semkagtn.musicdatamining.utils.JsonUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by semkagtn on 24.02.16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupsGetMembersResponse extends BaseVkResponse {

    @JsonProperty("response")
    private IdsList response;

    public IdsList getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
