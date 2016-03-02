package com.semkagtn.musicdatamining.vkapi.enums;

/**
 * Created by semkagtn on 24.02.16.
 */
public enum IdsSort {

    ID_ASC("id_asc"),
    ID_DESC("id_desc");

    private String value;

    IdsSort(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
