package com.umobilized.helpers.utils.validation.validators;

import com.umobilized.helpers.utils.validation.Validator;

/**
 * Created by tpaczesny on 2017-03-23.
 */

public class LengthValidator implements Validator<String> {

    private final int mMinLength;
    private final int mMaxLength;

    public LengthValidator(int minLength, int maxLength) {
        mMinLength = minLength;
        mMaxLength = maxLength;
    }

    public LengthValidator(int minLength) {
        mMinLength = minLength;
        mMaxLength = Integer.MAX_VALUE;
    }

    @Override
    public boolean isValid(String value) {
        int len = value != null ? value.length() : 0;

        return len >= mMinLength && len <= mMaxLength;
    }
}
