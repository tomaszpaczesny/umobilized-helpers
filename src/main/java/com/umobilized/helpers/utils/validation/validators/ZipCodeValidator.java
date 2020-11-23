package com.umobilized.helpers.utils.validation.validators;

import com.umobilized.helpers.utils.validation.Validator;

import java.util.regex.Pattern;

public class ZipCodeValidator implements Validator<String> {
    private static final String ZIP_CODE_REGEX = "^(?=[0-9]*$)(?:.{5}|.{9})$";
    private static final Pattern zipCodePattern = Pattern.compile(ZIP_CODE_REGEX);

    @Override
    public boolean isValid(String value) {
        if (value == null || value.isEmpty())
            return false;

        return zipCodePattern.matcher(value).matches();
    }
}
