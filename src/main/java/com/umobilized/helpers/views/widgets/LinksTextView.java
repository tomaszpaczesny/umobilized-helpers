package com.umobilized.helpers.views.widgets;

import android.content.Context;
import androidx.databinding.BindingAdapter;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import com.dinuscxj.ellipsize.EllipsizeTextView;

import java.util.ArrayList;

/**
 * Created by tpaczesny on 2017-03-02.
 */

public class LinksTextView extends EllipsizeTextView {
    private OnTextLinkClickedListener mListener;
    private boolean mJustClickedOnLink;
    protected final ArrayList<String> mLinks = new ArrayList<>();
    private LinkDrawer mCustomDrawer;

    @BindingAdapter("onLinkClicked")
    public static void bindSetOnLinkClickedListener(LinksTextView linksTextView, LinksTextView.OnTextLinkClickedListener listener) {
        linksTextView.setOnLinkClickedListener(listener);
    }

    public LinksTextView(Context context) {
        super(context);
        init();
    }

    public LinksTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setHighlightColor(Color.TRANSPARENT);
        setMovementMethod(LinkMovementMethod.getInstance());
        // On click listener for passive parts of text view to pass onClick to first view
        // in parent chain that has onClick listeners
        setOnClickListener((v) -> {
            // a bit of hack, so clicking on link does not fire parent's on click event
            // we whant this only for non-link parts of text
            if (mJustClickedOnLink) {
                mJustClickedOnLink = false;
                return;
            }

            ViewParent parent = v.getParent();
            while (parent != null && parent instanceof View) {
                View parentView = (View)parent;
                if (parentView.hasOnClickListeners()) {
                    parentView.callOnClick();
                    break;
                }
                parent = parent.getParent();
            }
        });
    }

    public void addExactLink(String link) {
        mLinks.add(link);
    }

    public void clearLinks() {
        mLinks.clear();
    }

    public void setOnLinkClickedListener(OnTextLinkClickedListener listener) {
        mListener = listener;
    }

    public void setCustomLinkDrawer(LinkDrawer drawer) {
        mCustomDrawer = drawer;
    }

    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        if (text != null && hasLinks(text.toString())) {
            text = convertToClickable(text.toString());
        }
        super.setText(text, type);
    }


    protected boolean hasLinks(String text) {
        if (mLinks != null) {
            for (String link : mLinks) {
                if (text.contains(link)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Spannable convertToClickable(String text) {
        SpannableString ss = new SpannableString(text);

        for (String link : mLinks) {
            setSpansForLinks(ss, text, link);
        }

        return ss;
    }

    protected SpannableString setSpansForLinks(SpannableString ss, String text, String link) {
        int idx = 0;

        while ((idx = text.indexOf(link, idx)) != -1) {
            ss.setSpan(new LinksClickableSpan(link),
                    idx,
                    idx+link.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            idx += link.length();
        }

        return ss;
    }

    public void applyLinks() {
        setText(getText());
    }


    public interface OnTextLinkClickedListener {
        void onTextLinkClicked(String link);
    }

    protected class LinksClickableSpan extends ClickableSpan {

        private final String mLink;

        public LinksClickableSpan(String link) {
            mLink = link;
        }

        @Override
        public void onClick(View view) {
            if (LinksTextView.this.mListener != null) {
                LinksTextView.this.mListener.onTextLinkClicked(mLink);
                LinksTextView.this.mJustClickedOnLink = true;
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            if (mCustomDrawer != null) {
                mCustomDrawer.updateDrawState(ds);
            } else {
                ds.setUnderlineText(false);
            }
        }

    }

    public interface LinkDrawer {
        void updateDrawState(TextPaint ds);
    }
}
