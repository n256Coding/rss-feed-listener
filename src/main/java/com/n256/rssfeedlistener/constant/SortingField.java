package com.n256.rssfeedlistener.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Maintains mapping between name of the sorting field comes in the request with the actual name of field in entity.
 * Reason to maintain this because directly exposing actual field names in entity to outside is not a good idea.
 */
public enum SortingField {
    PUBLISHED_DATE("publishedDate", "published_date"),
    TITLE("title", "item_title"),
    UPDATED_DATE("updateDate", "updated_date");

    @Getter
    private final String entityField;
    private final String inputField;

    SortingField(String entityField, String inputField) {
        this.entityField = entityField;
        this.inputField = inputField;
    }

    /**
     * Provides matching enum for given input field name
     *
     * @param field input sorting field name
     * @return matching object related to input sorting field
     */
    public static SortingField findByInputField(String field) {

        return Arrays.stream(values())
                .filter(x -> field.equalsIgnoreCase(x.inputField))
                .findAny()
                .orElse(null);
    }

    /**
     * @return list of available input field name list
     */
    public static List<String> getInputFieldList() {
        return Arrays.stream(values()).map(x -> x.inputField).collect(Collectors.toList());
    }

}
