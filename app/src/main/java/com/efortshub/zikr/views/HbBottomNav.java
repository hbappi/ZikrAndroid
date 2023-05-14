package com.efortshub.zikr.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HbBottomNav extends BottomNavigationView {
        private int mTranslateY = 0;
        public HbBottomNav(Context context) {
            super(context);
        }

        public HbBottomNav(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public HbBottomNav(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }


        public void setTranslationY(int value){
            mTranslateY = value;
            invalidate();

        }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw your custom view using the current translationY value
        canvas.translate(0, mTranslateY);
        // ...
    }
}
