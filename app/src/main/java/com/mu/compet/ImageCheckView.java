package com.mu.compet;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by goodn on 2016-08-31.
 */
public class ImageCheckView extends FrameLayout implements Checkable {

    private ImageView checkView;


    public ImageCheckView(Context context) {
        super(context);
        init();
    }

    public ImageCheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_check, this);
        checkView = (ImageView) findViewById(R.id.image_check);
    }

    boolean isChecked = false;

    private void drawCheck() {
        if (isChecked) {
            checkView.setImageResource(R.drawable.ic_checkbox_on);
        } else {
            checkView.setImageResource(R.drawable.ic_checkbox_off);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if (isChecked != checked) {
            isChecked = checked;
            drawCheck();
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }
}
