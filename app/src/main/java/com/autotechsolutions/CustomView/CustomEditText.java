package com.autotechsolutions.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.autotechsolutions.R;


/**
 * The type Custom edit text.
 */
public class CustomEditText extends AppCompatEditText {

    /**
     * Instantiates a new Custom edit text.
     *
     * @param context the context
     */
    public CustomEditText(Context context) {
        super(context);
    }

    /**
     * Instantiates a new Custom edit text.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    /**
     * Instantiates a new Custom edit text.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    /**
     * Set custom given fonts from xml. if not supplied then set default fonts
     *
     * @param ctx   context of an activity
     * @param attrs parameters from xml. i.e font style here
     */
    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
        String customFont = a.getString(R.styleable.TextViewPlus_customFont);
        if (customFont != null && customFont.length() > 0) {
            setTypeface(Typeface.createFromAsset(ctx.getResources().getAssets(), customFont));
        }
        a.recycle();
    }

    @Override
    public boolean performClick() {
        // do what you want
        return true;
    }
}