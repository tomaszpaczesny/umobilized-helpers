package com.umobilized.helpers.utils.validation.validators;

import com.umobilized.helpers.utils.validation.Validator;

import java.util.regex.Pattern;

/**
 * Created by tpaczesny on 2017-03-23.
 */

public class NonEmptyValidator implements Validator<String> {

    @Override
    public boolean isValid(String value) {
        return !(value == null || value.isEmpty());

    }
}
