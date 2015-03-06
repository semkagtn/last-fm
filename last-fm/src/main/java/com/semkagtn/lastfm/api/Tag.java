package com.semkagtn.lastfm.api;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by semkagtn on 3/6/15.
 */
public class Tag {

    private String name;
    private int reach;
    private int taggings;

    public String getName() {
        return name;
    }

    public int getReach() {
        return reach;
    }

    public int getTaggings() {
        return taggings;
    }

    void setName(String name) {
        this.name = name;
    }

    void setReach(int reach) {
        this.reach = reach;
    }

    void setTaggings(int taggings) {
        this.taggings = taggings;
    }

    Tag() {

    }

    public static class GetInfo extends Request<Tag> {

        public static Request<Tag> createRequest(String tag) {
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("method", "tag.getInfo"));
            parameters.add(new BasicNameValuePair("tag", tag));
            return new GetInfo(parameters);
        }

        private GetInfo(List<NameValuePair> parameters) {
            super(parameters);
        }

        @Override
        Tag parseResponse(JSONObject response) {
            JSONObject tagJson = response.optJSONObject("tag");
            if (tagJson == null) {
                throw new ParseResponseError("no 'tag' field", response);
            }
            String name = tagJson.optString("name");
            if (name == null) {
                throw new ParseResponseError("no 'name' field", response);
            }
            Tag tag = new Tag();
            tag.setName(name);
            tag.setReach(tagJson.optInt("reach", -1));
            tag.setTaggings(tagJson.optInt("taggings", -1));
            return tag;
        }
    }
}
