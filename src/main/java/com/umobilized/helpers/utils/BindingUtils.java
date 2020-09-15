package com.umobilized.helpers.utils;

import androidx.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.text.Html;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

/**
 * Created by tpaczesny on 2017-04-07.
 */

public class BindingUtils {

    @BindingAdapter("textHtml")
    public static void setTextHtml(TextView textView, String html) {
        if (html != null) {
            textView.setText(Html.fromHtml(html));
        }
    }

    @BindingAdapter("bind:error")
    public static void setError(TextInputLayout layout, String error) {
        if (layout.isErrorEnabled()) {
            layout.setError(error);
        }
    }


    @BindingAdapter("onAction")
    public static void setOnEditorActionListener(EditText editText, TextView.OnEditorActionListener listener) {
        editText.setOnEditorActionListener(listener);
    }

    @BindingAdapter("typeface")
    public static void setTypeface(TextView textView, String typefaceName) {
        if (typefaceName != null) {
            Typeface typeface = FontsHelper.getFont(typefaceName);
            if (typeface != null) {
                textView.setTypeface(typeface);
            }
        }
    }
}
