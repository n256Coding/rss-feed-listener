package com.n256.rssfeedlistener.util;

import com.n256.rssfeedlistener.constant.SortingField;
import com.n256.rssfeedlistener.exception.ValidationFailedException;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Sort;

/**
 * A utility class which handles validation of input parameters.
 */
public final class InputValidatorUtil {

    private InputValidatorUtil() {
    }

    /**
     * Validates the page number input.
     *
     * @param pageString page number in String format.
     * @param defaultValue default page number in case provided page number is empty.
     * @return validated page number in integer format.
     */
    public static int validatePage(String pageString, int defaultValue) {
        if (pageString.isEmpty() || NumberUtils.isCreatable(pageString)) {
            return NumberUtils.toInt(pageString, defaultValue);

        } else {
            throw new ValidationFailedException("Page number must be a numeric value");
        }
    }

    /**
     * Validates the page size input field.
     *
     * @param sizeString page size in String format.
     * @param defaultValue default page size in case if provided page size is empty.
     * @return validated page size in integer format.
     */
    public static int validatePageSize(String sizeString, int defaultValue) {
        if (sizeString.isEmpty() || NumberUtils.isCreatable(sizeString)) {
            return NumberUtils.toInt(sizeString, defaultValue);

        } else {
            throw new ValidationFailedException("Page size must be a numeric value.");
        }
    }

    /**
     * Validates the sort by field in the request.
     *
     * @param sortBy name of the field that the results needs to be sorted.
     * @param defaultValue default name of the field in case if provided field name is empty.
     * @return validated sorting field.
     */
    public static SortingField validateSortBy(String sortBy, String defaultValue) {

        if (sortBy.isEmpty()) {
            return SortingField.findByInputField(defaultValue);

        } else {

            SortingField sortingField = SortingField.findByInputField(sortBy);

            if (sortingField != null) {
                return sortingField;

            } else {
                throw new ValidationFailedException(
                        String.format("Invalid or unsupported sorting field. Supported Fields [%s]",
                                SortingField.getInputFieldList())
                );
            }
        }

    }

    /**
     * Validates the sorting direction in the request field.
     *
     * @param sortDirection sorting direction that the results needs to be sorted.
     * @param defaultValue default sorting direction in case if provided sorting direction is empty.
     * @return validated sorting direction in {@link Sort.Direction} format.
     */
    public static Sort.Direction validateSortingDirection(String sortDirection, String defaultValue) {

        if (sortDirection.isEmpty()) {
            return Sort.Direction.valueOf(defaultValue.toUpperCase());

        } else {

            try {
                return Sort.Direction.valueOf(sortDirection.toUpperCase());

            } catch (IllegalArgumentException ex) {
                throw new ValidationFailedException("Invalid sorting direction. Supported directions [asc, desc]");
            }
        }
    }
}
