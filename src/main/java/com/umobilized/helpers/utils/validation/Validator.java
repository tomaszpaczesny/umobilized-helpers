package com.umobilized.helpers.utils.validation;

import java.io.Serializable;

/**
 * Created by tpaczesny on 2017-03-23.
 */

public interface Validator<T> extends Serializable{
    boolean isValid(T value);
}
