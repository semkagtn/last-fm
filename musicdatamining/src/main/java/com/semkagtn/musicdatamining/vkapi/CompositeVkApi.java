package com.semkagtn.musicdatamining.vkapi;

import com.semkagtn.musicdatamining.vkapi.response.AudioSearchResponse;

/**
 * Created by semkagtn on 24.02.16.
 */
public class CompositeVkApi {

    private static final long CAPTCHA_WAIT = 60_000 * 3;

    private long captchaExpires;
    private VkApi officialApi;
    private VkApi unofficialApi;

    public CompositeVkApi(VkApi officialApi, VkApi unofficialApi) {
        this.officialApi = officialApi;
        this.unofficialApi = unofficialApi;
        this.captchaExpires = System.currentTimeMillis();
    }

    public AudioSearchResponse audioSearch(String query, Boolean autoComplete, Integer count) {
        AudioSearchResponse response;
        try {
            if (System.currentTimeMillis() >= captchaExpires) {
                response = officialApi.audioSearchNew(query, autoComplete, count, 0);
            } else {
                response = unofficialApi.audioSearchNew(query, autoComplete, count, 0);
            }
        } catch (VkApi.CaptchaNeededError e) {
            captchaExpires = System.currentTimeMillis() + CAPTCHA_WAIT;
            response = unofficialApi.audioSearchNew(query, autoComplete, count, 0);
        }
        return response;
    }
}
