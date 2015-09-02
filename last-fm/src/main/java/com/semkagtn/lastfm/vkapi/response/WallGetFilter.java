package com.semkagtn.lastfm.vkapi.response;

/**
 * Created by semkagtn on 01.09.15.
 */
public enum WallGetFilter {

    ALL("all"),
    OWNER("owner"),
    OTHERS("others"),
    POSTPONED("postponed"),
    SUGGESTS("suggests");

    private String value;

    WallGetFilter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
