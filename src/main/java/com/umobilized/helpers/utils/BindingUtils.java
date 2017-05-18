package com.umobilized.helpers.utils;

import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.text.Html;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by tpaczesny on 2017-04-07.
 */

public class BindingUtils {

    @BindingAdapter("bind:textHtml")
    public static void setTextHtml(TextView textView, String html) {
        if (html != null) {
            textView.setText(Html.fromHtml(html));
        }
    }

    @BindingAdapter("bind:onAction")
    public static void setOnEditorActionListener(EditText editText, TextView.OnEditorActionListener listener) {
        editText.setOnEditorActionListener(listener);
    }

    @BindingAdapter("bind:typeface")
    public static void setTypeface(TextView textView, String typefaceName) {
        if (typefaceName != null) {
            Typeface typeface = FontsHelper.getFont(typefaceName);
            if (typeface != null) {
                textView.setTypeface(typeface);
            }
        }
    }
}
