package com.opendev.buket.club.tools;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;




public class MySpannable extends ClickableSpan {

    private boolean isUnderline = true;

    /**
     * Constructor
     */
    public MySpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {


        ds.setUnderlineText(isUnderline);
        ds.setColor(Color.parseColor("#0087FF"));

    }

    @Override
    public void onClick(View widget) {

    }

}

