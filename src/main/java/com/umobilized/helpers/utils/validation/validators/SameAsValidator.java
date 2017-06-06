package com.umobilized.helpers.utils.validation.validators;

import com.umobilized.helpers.utils.validation.ObservableValidationField;
import com.umobilized.helpers.utils.validation.Validator;

/**
 * Validator that is valid if value is equal to value of reference field.
 *
 * Created by tpaczesny on 2017-06-05.
 */

public class SameAsValidator implements Validator<String> {
    private ObservableValidationField mRef;

    public SameAsValidator(ObservableValidationField<String> ref) {
        mRef = ref;
    }

    @Override
    public boolean isValid(String value) {
        return (value == null && mRef.getValue() == null) || (value != null && value.equals(mRef.getValue()));
    }
}