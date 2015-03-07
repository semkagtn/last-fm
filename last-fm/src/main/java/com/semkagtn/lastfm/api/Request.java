package com.semkagtn.lastfm.api;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by semkagtn on 3/6/15.
 */
public abstract class Request<T> {

    private List<NameValuePair> parameters;
    private String hash;

    protected Request(List<NameValuePair> parameters) {
        this.parameters = parameters;
        this.hash = generateSha1();
    }

    List<NameValuePair> getParameters() {
        return parameters;
    }

    public String hash() {
        return hash;
    }

    private String generateSha1() {
        String parametersString = parameters.stream()
                .map(x -> x.getName() + "" + x.getValue())
                .reduce("", (x, y) -> x + y);
        return DigestUtils.shaHex(parametersString);
    }

    abstract T parseResponse(JSONObject response);

    public static class ParseResponseError extends Error {

        public ParseResponseError(String message, JSONObject jsonObject) {
            super(message + ": " + jsonObject.toString());
        }
    }
}
