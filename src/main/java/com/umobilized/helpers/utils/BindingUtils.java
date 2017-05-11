package com.umobilized.helpers.utils;

import android.databinding.BindingAdapter;
import android.text.Html;
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
}
