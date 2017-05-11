package com.umobilized.helpers.utils.validation.validators;

import com.umobilized.helpers.utils.validation.Validator;

import java.util.regex.Pattern;

/**
 * Created by tpaczesny on 2017-03-23.
 */

public class UrlValidator implements Validator<String> {

    private static final String URL_REGEX = "(?i)((https?:\\/\\/)|(www\\.))?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w\\.-]*)*\\/?(\\s|$)";

    private static final Pattern urlPattern = Pattern.compile(URL_REGEX);

    @Override
    public boolean isValid(String value) {
        if (value == null || value.isEmpty())
            return false;

        return urlPattern.matcher(value).matches();
    }
}
