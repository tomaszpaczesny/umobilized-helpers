package com.umobilized.helpers.utils.validation.validators;

import com.umobilized.helpers.utils.validation.Validator;

/**
 * Like LengthValidator but passes validation if value is null/empty
 *
 * Created by tpaczesny on 2017-03-23.
 */

public class OptionalLengthValidator implements Validator<String> {

    private final int mMinLength;
    private final int mMaxLength;

    public OptionalLengthValidator(int minLength, int maxLength) {
        mMinLength = minLength;
        mMaxLength = maxLength;
    }

    public OptionalLengthValidator(int minLength) {
        mMinLength = minLength;
        mMaxLength = Integer.MAX_VALUE;
    }

    @Override
    public boolean isValid(String value) {
        if (value == null || value.isEmpty())
            return true;

        int len = value.length();
        return len >= mMinLength && len <= mMaxLength;
    }
}
