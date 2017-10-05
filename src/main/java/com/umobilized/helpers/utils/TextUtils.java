package com.umobilized.helpers.utils;

import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.util.Locale;

/**
 * Created by tpaczesny on 2017-03-29.
 */

public class TextUtils {

    public static String trim(String str) {
        return str != null ? str.trim() : null;
    }

    public static boolean hasText(String str) {
        return str != null && !str.isEmpty();
    }

    public static boolean hasInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Convert CharSequence into a Spannable with a given color.
     * Useful to pass color where custom styles are not permitted (e.g. menus)
     *
     * @param text
     * @param color
     * @return
     */
    public static Spannable colorText(CharSequence text, int color) {
        SpannableString s = new SpannableString(text);
        s.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return s;
    }

    /**
     * Convert CharSequence into a Spannable with a given color applied to sequences matching the pattern (ignore case).
     *
     * @param text
     * @param sequence
     * @param color
     * @return
     */
    public static Spannable colorTextSequences(CharSequence text, String sequence, int color) {
        return applySpanToTextSequences(text, sequence, new ForegroundColorSpan(color));
    }

    /**
     * Convert CharSequence into a Spannable with style effect (bold, italic etc) applied to sequences matching the pattern (ignore case).
     *
     * @param text
     * @param typefaceStyle style constant from Typeface class
     * @return
     */
    public static Spannable styleTextSequences(CharSequence text, String sequence, int typefaceStyle) {
        return applySpanToTextSequences(text, sequence, new StyleSpan(typefaceStyle));
    }

    public static Spannable applySpanToTextSequences(CharSequence text, String sequence, Object span) {
        if (text == null) {
            return null;
        }
        SpannableString s = new SpannableString(text);

        String textStrToLower = text.toString().toLowerCase();

        if (textStrToLower.length() > 0) {
            if (sequence != null && sequence.length() > 0) {
                String sequenceToLower = sequence.toLowerCase();

                int startIdx = 0;
                int idx;
                while ((idx = textStrToLower.indexOf(sequenceToLower, startIdx)) != -1) {
                    s.setSpan(span, idx, idx + sequenceToLower.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    startIdx = idx + 1;
                }
            }
        }
        return s;
    }

    /**
     * Format millisecond duration into 01:23 format
     * @param durationMs
     * @return
     */
    public static String formatDuration(int durationMs) {
        int minutes = durationMs / 60000;
        int seconds = (durationMs / 1000) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    /**
     * Returns initials from given text (like user full name) or empty string if not possible.
     * @param text
     * @return
     */

    public static String toInitials(String text) {
        if (text != null && !text.isEmpty()) {
            if (text.length() == 1) {
                return Character.toString(text.charAt(0));
            }

            String input = text.trim();
            int spaceIdx = input.indexOf(" ");
            if (spaceIdx == -1 || spaceIdx == input.length()-1) {
                return input.substring(0, 2).toUpperCase();
            } else {
                return (String.valueOf(input.charAt(0)) + String.valueOf(input.charAt(spaceIdx+1))).toUpperCase();
            }
        } else {
            return "";
        }
    }


    public static InputFilter getNoWhitespaceInputFilter() {
        return (source, start, end, dest, dstart, dend) -> {
            boolean keepOriginal = true;
            StringBuilder sb = new StringBuilder(end - start);
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (!Character.isWhitespace(c))
                    sb.append(c);
                else
                    keepOriginal = false;
            }
            if (keepOriginal)
                return null;
            else {
                if (source instanceof Spanned) {
                    SpannableString sp = new SpannableString(sb);
                    android.text.TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                    return sp;
                } else {
                    return sb;
                }
            }
        };
    }


}
