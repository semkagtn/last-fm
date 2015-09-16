package com.semkagtn.musicdatamining.vkapi.response;

import com.semkagtn.musicdatamining.utils.JsonUtils;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Created by semkagtn on 30.08.15.
 */
public class UsersGetResponse extends BaseVkResponse {

    @JsonProperty("response")
    private List<UserItem> response;

    public List<UserItem> getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
