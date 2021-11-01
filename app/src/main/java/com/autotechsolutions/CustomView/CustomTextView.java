package com.autotechsolutions.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.autotechsolutions.R;


/**
 * The type Custom text view.
 */
public class CustomTextView extends AppCompatTextView {

    /**
     * Default kerning values which can be used for convenience
     */
    public class Kerning {
        public final static float NO_KERNING = 0;
        public final static float SMALL = 1;
        public final static float MEDIUM = 4;
        public final static float LARGE = 6;
    }

    private float mKerningFactor = Kerning.NO_KERNING;
    private CharSequence mOriginalText;

    /**
     * Instantiates a new Custom text view.
     *
     * @param context the context
     */
    public CustomTextView(Context context) {
        super(context.getApplicationContext());
    }

    /**
     * Instantiates a new Custom text view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context.getApplicationContext(), attrs);
        setCustomFont(context.getApplicationContext(), attrs);
    }

    /**
     * Instantiates a new Custom text view.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context.getApplicationContext(), attrs, defStyle);
        setCustomFont(context.getApplicationContext(), attrs);
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

//    @Override
//    public boolean performClick() {
//        // do what you want
//        return true;
//    }
}