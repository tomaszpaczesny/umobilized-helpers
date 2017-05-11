package com.umobilized.helpers.utils.validation.validators;

import com.umobilized.helpers.utils.validation.Validator;

import java.util.regex.Pattern;

/**
 * Created by tpaczesny on 2017-03-23.
 */

public class EmailValidator implements Validator<String> {

    private static final String EMAIL_REGEX = "^\\S+@\\S+\\.\\S+$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

    @Override
    public boolean isValid(String value) {
        if (value == null || value.isEmpty())
            return false;

        return emailPattern.matcher(value).matches();
    }
}
