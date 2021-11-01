package com.autotechsolutions.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.autotechsolutions.R;


/**
 * The type Custom button.
 */
public class CustomButton extends AppCompatButton {

    /**
     * Instantiates a new Custom button.
     *
     * @param context the context
     */
    public CustomButton(Context context) {
        super(context);
    }

    /**
     * Instantiates a new Custom button.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    /**
     * Instantiates a new Custom button.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
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

}