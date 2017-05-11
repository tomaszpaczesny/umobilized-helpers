package com.umobilized.helpers.utils.validation;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableField;

import com.umobilized.helpers.BR;

import java.io.Serializable;

/**
 * Created by tpaczesny on 2017-03-23.
 */

public class ObservableValidationField<T> extends BaseObservable implements Serializable{

    private T mValue;
    private Validator<T> mValidator;
    private String mValidationError; // null for no error

    public ObservableValidationField() {
        super();
    }

    public ObservableValidationField(Validator<T> validator) {
        super();
        mValidator = validator;
    }

    @Bindable
    public T getValue() {
        return mValue;
    }

    public void setValue(T value) {
        this.mValue = value;
        notifyPropertyChanged(BR.value);
    }

    @Bindable
    public String getError() {
        return mValidationError;
    }

    public void setError(String error) {
        mValidationError = error;
        notifyPropertyChanged(BR.error);
    }

    public void setValueChangedListener(final OnValueChangedListener listener) {
        addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == BR.value) {
                    listener.valueChanged();
                }
            }
        });
    }

    public boolean validate(boolean onlyClearErrors, String errorMessage) {
        if (isValid()) {
            setError(null);
            return false;
        } else {
            if (!onlyClearErrors) {
                setError(errorMessage);
            }
            return true;
        }
    }

    public boolean isValid() {
        if (mValidator != null) {
            return mValidator.isValid(getValue());
        } else {
            return true;
        }
    }
}
